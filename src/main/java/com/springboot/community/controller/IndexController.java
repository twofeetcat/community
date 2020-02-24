package com.springboot.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller //这个注解会让spring自动扫描这个类，并当成一个bean去管理
public class IndexController {
    @GetMapping("/")
    public String hello(){
        return "index";
    }
}
