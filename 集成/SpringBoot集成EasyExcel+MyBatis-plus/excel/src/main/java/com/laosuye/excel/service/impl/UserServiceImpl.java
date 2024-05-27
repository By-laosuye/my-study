package com.laosuye.excel.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laosuye.excel.entity.User;
import com.laosuye.excel.dao.UserDao;
import com.laosuye.excel.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台用户表 服务实现类
 * </p>
 *
 * @author laosuye
 * @since 2024-05-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

}
