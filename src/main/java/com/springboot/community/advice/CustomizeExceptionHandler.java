package com.springboot.community.advice;

import com.alibaba.fastjson.JSON;
import com.springboot.community.dto.ResultDto;
import com.springboot.community.exception.CustomizeErrorCode;
import com.springboot.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handle(Throwable ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        //判断是url地址引起的错误还是json引起的错误
        if ("application/json".equals(contentType)){
            ResultDto resultDto;
            //返回Json
            if (ex instanceof CustomizeException){
                resultDto = ResultDto.errorOf((CustomizeException) ex);
            }else {
                resultDto = ResultDto.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        }else {
            //错误页面跳转
            if (ex instanceof CustomizeException){
                model.addAttribute("message", ex.getMessage());
            }else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
