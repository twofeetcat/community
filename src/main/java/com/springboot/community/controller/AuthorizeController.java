package com.springboot.community.controller;

import com.springboot.community.dto.AccessTokenDto;
import com.springboot.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired //把spring容器里面已经实例化好的实例加载到当前使用的上下文
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id("d5c54e7e70dc5d5646a6");
        accessTokenDto.setClient_secret("83d3f0ced7fb44a52c44315323e67db9565834ed");
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri("https://github.com/login/oauth/access_token");
        accessTokenDto.setState(state);
        githubProvider.getAccessToken(accessTokenDto);
        return "index";
    }
}
