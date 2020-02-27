"# community" 
## 资料
[spring 文档]https://spring.io/guides
[mybatis-spring-boot-autoconfigure]http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/

## 工具
[阿里云镜像下载git]https://npm.taobao.org/mirrors/git-for-windows/
[bookstrap中文版官网v3]https://v3.bootcss.com/

[bulid OAuth]https://developer.github.com/apps/building-oauth-apps/

[okhttp官网]https://square.github.io/okhttp/

##脚本
```sql
create table USER
(
    ID           INTEGER default NEXT VALUE FOR "PUBLIC"."SYSTEM_SEQUENCE_8749B5BB_CCA8_4786_9A78_968148AD178F" auto_increment,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    constraint USER_PK
        primary key (ID)
);
```