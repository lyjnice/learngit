package com.yanxiu.demo.model;

import java.io.Serializable;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long uid;

    private String passport;

    private String password;

    private Byte actiFlag;

    private String personalId;

    private String email;

    private String mobile;

    private String mapWebsite;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport == null ? null : passport.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Byte getActiFlag() {
        return actiFlag;
    }

    public void setActiFlag(Byte actiFlag) {
        this.actiFlag = actiFlag;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId == null ? null : personalId.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getMapWebsite() {
        return mapWebsite;
    }

    public void setMapWebsite(String mapWebsite) {
        this.mapWebsite = mapWebsite == null ? null : mapWebsite.trim();
    }
}