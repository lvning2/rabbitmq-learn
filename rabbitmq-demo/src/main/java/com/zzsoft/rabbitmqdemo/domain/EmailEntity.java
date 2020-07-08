package com.zzsoft.rabbitmqdemo.domain;

import java.io.Serializable;


public class EmailEntity implements Serializable {

    private int id;
    private String host;
    private String password;
    private String username;
    private String msg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "EmailEntity{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
