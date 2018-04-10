package com.galaxy.cloud.consul.producer;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import org.apache.commons.lang3.StringUtils;
import org.cfg4j.source.context.propertiesprovider.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
public class ConsulProducerApplication implements CommandLineRunner{

    private static final Logger LOG = LoggerFactory.getLogger(ConsulProducerApplication.class);

    @Value("${spring.cloud.consul.host}")
    private String host;

    @Value("${spring.cloud.consul.port}")
    private int port;

    @Value("${spring.cloud.consul.config.prefix}")
    private String prefix;

    @Value("${spring.cloud.consul.config.name}")
    private String name;

    private KeyValueClient kvClient;

    @Override
    public void run(String... args) throws Exception {

        LOG.info("Connecting to Consul client at " + host + ":" + port);

        HostAndPort hostAndPort = HostAndPort.fromParts(host, port);
        Consul consul = Consul.builder().withHostAndPort(hostAndPort).build();
        kvClient = consul.keyValueClient();

        PropertiesProviderSelector propertiesProviderSelector = new PropertiesProviderSelector(
                new PropertyBasedPropertiesProvider(), new YamlBasedPropertiesProvider(), new JsonBasedPropertiesProvider()
        );


        Resource resource = new ClassPathResource("application.yml");
        File file = resource.getFile();

        if (file.toString().endsWith(".yml") || file.toString().endsWith(".properties")) {
            LOG.info("found config file: " + file.getName() + " in context " + file.getParentFile().getPath());

            try (InputStream input = new FileInputStream(file)) {

                Properties properties = new Properties();
                PropertiesProvider provider = propertiesProviderSelector.getProvider(file.getName());
                properties.putAll(provider.getProperties(input));

                for (Map.Entry<Object, Object> prop : properties.entrySet()) {
                    String key = prefix + "/" + name + "/" + prop.getKey().toString();
                    String value = prop.getValue().toString();
                    // 如果consul中已存在键值，则不重新设置
                    String received = kvClient.getValueAsString(key).orElse("");
                    if (StringUtils.isBlank(received)){
                        kvClient.putValue(key, value);
                    }
                }
            } catch (IOException e) {
                LOG.error("Unable to load properties from file: " + file, e);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsulProducerApplication.class, args);
    }
}