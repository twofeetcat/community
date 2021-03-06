package com.springboot.community.dto;

import com.springboot.community.exception.CustomizeErrorCode;
import com.springboot.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDto {
    //code传回异常编码，来跳转网页
    private Integer code;
    private String message;

    //错误源自
    public static ResultDto errorOf(Integer code, String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }

    public static ResultDto errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDto errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }

    public static ResultDto okOf(){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        return resultDto;
    }
}
