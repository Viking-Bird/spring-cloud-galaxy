package com.galaxy.cloud.consul.producer.web;

import com.galaxy.cloud.consul.producer.feign.TestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试远程调用
 *
 * Created by wangpeng on 2018/4/2.
 */
@RestController
@RequestMapping("/ping")
public class TestFeginController {

    @Autowired
    private TestFeign testFeign;

    public TestFeginController(){
        //For Spring
    }

    @GetMapping
    public String doAlive() {
        return "Alive!";
    }

    @GetMapping(value = "/rest/feign")
    public String doRestAliveUsingFeign() {
        return testFeign.doAlive();
    }

    @GetMapping(value = "/rest/value")
    public String getConfigFromValue(){
        return this.testFeign.getConfigFromValue();
    }
}
