package com.springboot.community.controller;

import com.springboot.community.dto.PaginationDto;
import com.springboot.community.mapper.UserMapper;
import com.springboot.community.model.User;
import com.springboot.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    //代表我希望它访问/profile这个页面的时候来访问我的这个方法
    @GetMapping("/profile/{action}")
    //String的意思就是返回对应页面
    public String profile(@PathVariable(name = "action") String action, Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size){
        User user = null;
        Cookie[] cookies = request.getCookies();//从服务器拿到我们传过去的cookies
        if (cookies != null){
            for (Cookie cookie:cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user = userMapper.findUserByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if (user == null){
            return "redirect:/";
        }
        if ("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        }else if ("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        PaginationDto paginationDto = questionService.listByUserId(user.getId(), page, size);
        model.addAttribute("pagination", paginationDto);
        return "profile";
    }
}
