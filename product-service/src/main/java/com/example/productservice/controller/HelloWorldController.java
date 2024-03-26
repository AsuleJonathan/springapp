package com.asule.productservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api")
@RestController
@Slf4j
public class HelloWorldController {
    @GetMapping("hello")
    public String getMethodName(@RequestParam(required=false) String param){
        log.info("Received params {}",param);
        return "Hello World!";
    }

}
