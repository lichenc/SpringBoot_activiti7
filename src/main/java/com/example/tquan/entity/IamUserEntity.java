package com.example.tquan.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by chenjin on 2021/6/21 10:11
 */
@Component
@PropertySource(value = {"classpath:iam.properties"})
@ConfigurationProperties(prefix = "user")
public class IamUserEntity {
    private String addr;
    private String id;
    private String type;
    private String charset;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
