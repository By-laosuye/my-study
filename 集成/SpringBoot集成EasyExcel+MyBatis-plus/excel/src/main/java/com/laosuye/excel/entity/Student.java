package com.laosuye.excel.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * (Student)表实体类
 *
 * @author makejava
 * @since 2024-05-25 21:19:14
 */
@SuppressWarnings("serial")
public class Student extends Model<Student> {
    //用户id
    private String userId;
    //用户姓名
    private String userName;
    //真实姓名
    private String realName;
    //性别
    private String sex;
    //手机号
    private String phone;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}

