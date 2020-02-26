package com.springboot.community.provider;

import com.alibaba.fastjson.JSON;
import com.springboot.community.dto.AccessTokenDto;
import com.springboot.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component //把路径仅仅上传到spring的上下文
public class GithubProvider {
    public String getAccessToken(AccessTokenDto accessTokenDto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token?client_id="+accessTokenDto.getClient_id()+"&client_secret="+accessTokenDto.getClient_secret()+"&code="+accessTokenDto.getCode()+"&redirect_uri="+accessTokenDto.getRedirect_uri()+"&state="+accessTokenDto.getState())
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String tooken = string.split("&")[0].split("=")[1]; //先取到前半部分的access_token，再用等号划分取到后面的token码
            return tooken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();

        try  {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);//直接将string转换成java类项目
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
