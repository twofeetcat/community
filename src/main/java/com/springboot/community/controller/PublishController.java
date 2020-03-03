package com.springboot.community.controller;

import com.springboot.community.mapper.QuestionMapper;
import com.springboot.community.mapper.UserMapper;
import com.springboot.community.model.Question;
import com.springboot.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    //获得mapper
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    //获得userMapper
    private UserMapper userMapper;

    @GetMapping("/publish") //get获得页面，post处理请求
    public String publish(){
        return "publish";
    }

    //接受到publish.html中传来的值，并进行以下操作
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model
    ){
        //model能在页面上直接获取到,用于回显
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if(title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }if(description == null || description == ""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }if(tag == null || tag == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        //拿到user对象，验证是否登录
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
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        //注入question的值
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        //创建question对象
        questionMapper.create(question);
        //没有异常的话重定向回首页
        return "redirect:/";
    }
}
