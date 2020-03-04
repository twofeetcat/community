package com.springboot.community.controller;

import com.springboot.community.dto.PaginationDto;
import com.springboot.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller //这个注解会让spring自动扫描这个类，并当成一个bean去管理
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size){
        //获取首页问题信息,我们只能拿到question对象而不能直接拿到questionDto，所以就出现了service层
        //pagination:此时获取到的已经是包含分页信息的整个对象了
        PaginationDto pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);
        //每次index.html发起请求，都会反应到这个页面，
        return "index";
    }
}
