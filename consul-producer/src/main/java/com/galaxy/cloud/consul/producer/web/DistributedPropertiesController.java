package com.galaxy.cloud.consul.producer.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
@RefreshScope
public class DistributedPropertiesController {

    Logger logger = LoggerFactory.getLogger(DistributedPropertiesController.class);

    @Value("${author}")
    String value;

    @Value("${film}")
    String film;

    @GetMapping("/getConfigFromValue")
    public String getConfigFromValue() {
        logger.info("some message");
        return value + film;
    }

}
