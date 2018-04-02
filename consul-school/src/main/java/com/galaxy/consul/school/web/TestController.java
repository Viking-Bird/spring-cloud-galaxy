package com.galaxy.consul.school.web;

import com.galaxy.consul.school.feign.TestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangpeng on 2018/4/2.
 */
@RestController
@RequestMapping("/ping")
public class TestController {

    @Autowired
    private TestFeign testFeign;

    public TestController(){
        //For Spring
    }

    @RequestMapping
    public String doAlive() {
        return "Alive!";
    }

    @RequestMapping("/rest/feign")
    public String doRestAliveUsingFeign() {
        return testFeign.doAlive();
    }
}
