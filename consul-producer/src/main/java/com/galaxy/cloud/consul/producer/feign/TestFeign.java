package com.galaxy.cloud.consul.producer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wangpeng on 2018/4/2.
 */
@FeignClient("pig-consul")
public interface TestFeign {

    @GetMapping(value="/ping")
    String doAlive();

    @GetMapping(value = "/config/getConfigFromValue")
    String getConfigFromValue();
}
