package com.springboot.community.dto;

import com.springboot.community.model.User;
import lombok.Data;

@Data
public class QuestionDto {
    //和question近乎完全一样，作为一个过渡类，多了user，方便我们通过question直接获得到user中想要的属性
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private String tag;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private User user;
}
