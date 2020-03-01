package com.springboot.community.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String token;
    private String accountId;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
