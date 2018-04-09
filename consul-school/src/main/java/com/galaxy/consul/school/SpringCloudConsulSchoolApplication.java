package com.galaxy.consul.school;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
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
import java.util.Map;
import java.util.Properties;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
public class SpringCloudConsulSchoolApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SpringCloudConsulSchoolApplication.class);

    @Value("${globalPrefix}")
    private String globalPrefix;

    @Value("${spring.cloud.consul.host}")
    private String host;

    @Value("${spring.cloud.consul.port}")
    private int port;

    private KeyValueClient kvClient;
    private Map<String, String> consulValues;

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
                    kvClient.putValue("config/" + globalPrefix + "/data", prop.getKey().toString() + ": " + prop.getValue().toString());
                }

            } catch (IOException e) {
                LOG.error("Unable to load properties from file: " + file, e);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConsulSchoolApplication.class, args);
    }
}