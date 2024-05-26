package com.laosuye.excel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version：1.0
 * @Author：yelixian
 * @description：
 * @data：2024-05-24-上午9:34
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String userId;
    private String userName;
    private String realName;
    private String sex;
    private String mobile;
}
