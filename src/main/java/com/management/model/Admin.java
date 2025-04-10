package com.management.model;

public class Admin {
    private int adminid;
    private String adminname;
    private String password;
    private String email;

    // 构造函数
    public Admin( String adminname, String password, String email) {
        this.adminname = adminname;
        this.password = password;
        this.email = email;
    }

    // 构造带id的构造函数
    public Admin(int adminid, String adminname, String password, String email) {
        this.adminid = adminid;
        this.adminname = adminname;
        this.password = password;
        this.email = email;
    }

    // getter and setter
    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public int getAdminid() {
        return adminid;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
