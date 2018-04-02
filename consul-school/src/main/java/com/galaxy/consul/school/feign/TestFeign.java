package com.galaxy.consul.school.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wangpeng on 2018/4/2.
 */
@FeignClient("pig-consul")
public interface TestFeign {

    @RequestMapping(value="/ping", method = RequestMethod.GET)
    String doAlive();
}
