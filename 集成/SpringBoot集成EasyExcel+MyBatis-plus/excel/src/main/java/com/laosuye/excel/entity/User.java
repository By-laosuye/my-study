package com.laosuye.excel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 平台用户表
 * </p>
 *
 * @author laosuye
 * @since 2024-05-24
 */
@Data
@TableName("uc_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String appId;

    private String code;

    private String userName;

    private String account;

    private String password;

    private Date passwordExpiryTime;

    private String pinyin;

    private String gender;

    private String email;

    private String phone;

    private String cardNum;

    private String isAdmin;

    private String userType;

    private String addrss;

    private String postCode;

    private Date createDate;

    private Date modifyDate;

    private String status;

    private String remark;

    private String mdmId;

    private String createId;

    private String empId;

    private String openId;

    private String authenticationType;

    private String orgId;

    private String orgCode;

    private String companyId;
}
