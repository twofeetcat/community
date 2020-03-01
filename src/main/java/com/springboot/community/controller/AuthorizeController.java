package com.springboot.community.controller;

import com.springboot.community.dto.AccessTokenDto;
import com.springboot.community.dto.GithubUser;
import com.springboot.community.mapper.UserMapper;
import com.springboot.community.model.User;
import com.springboot.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired //把spring容器里面已经实例化好的实例加载到当前使用的上下文
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                            HttpServletResponse response){
        //session是通过request得到的
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null && githubUser.getId() != null){
            User user = new User(); //获取用户信息
            String token = UUID.randomUUID().toString();  //想让token代替曾经的session
            user.setToken(token); //生成一个token，并将token放进user对象中，存入数据库
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setName(githubUser.getName());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user); //存入数据库
            response.addCookie(new Cookie("token", token));
            //不加前缀的话，只会把页面渲染到index，但是用户信息会出现在地址上，加上后，相当于重定向到index页面，地址也会转回index
            return "redirect:/";
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }
}
