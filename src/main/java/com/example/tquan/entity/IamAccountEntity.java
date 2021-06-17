package com.example.tquan.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:iam.properties"})
@ConfigurationProperties(prefix = "account")
public class IamAccountEntity {
    private String addr;
    private String pwd;
    private String status;
    private int strLength;
    private int pwdRank;
    private String randomSwitch;
    private String immobilizationSwitch;
    private String pwdDefault;
    private String type;
    private String charset;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStrLength() {
        return strLength;
    }

    public void setStrLength(int strLength) {
        this.strLength = strLength;
    }

    public int getPwdRank() {
        return pwdRank;
    }

    public void setPwdRank(int pwdRank) {
        this.pwdRank = pwdRank;
    }

    public String getRandomSwitch() {
        return randomSwitch;
    }

    public void setRandomSwitch(String randomSwitch) {
        this.randomSwitch = randomSwitch;
    }

    public String getImmobilizationSwitch() {
        return immobilizationSwitch;
    }

    public void setImmobilizationSwitch(String immobilizationSwitch) {
        this.immobilizationSwitch = immobilizationSwitch;
    }

    public String getPwdDefault() {
        return pwdDefault;
    }

    public void setPwdDefault(String pwdDefault) {
        this.pwdDefault = pwdDefault;
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
