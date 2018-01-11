package com.example.hunter.mode;

import cn.bmob.v3.BmobObject;

public class HunterData extends BmobObject {

    private Integer id;
    private Integer status;
    private String user;
    private String title;
    private String image;

    public HunterData() {
    }

    public HunterData(Integer id, Integer status, String user, String title, String image) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.title = title;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
