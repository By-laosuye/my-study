package com.laosuye.excel.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * <p>
 * 用户授权平台
 * </p>
 *
 * @author laosuye
 * @since 2024-05-25
 */
@Data
@TableName("uc_user_app_relation")
public class UserAppRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private String appId;

    private String status;

    private String companyId;

    private String orgId;

    private Date createDate;

    private Date modifyDate;

    private String createId;
}
