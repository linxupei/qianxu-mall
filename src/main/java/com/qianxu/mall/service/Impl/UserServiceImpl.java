package com.qianxu.mall.service.Impl;

import com.qianxu.mall.exception.QianxuMallException;
import com.qianxu.mall.exception.QianxuMallExceptionEnum;
import com.qianxu.mall.model.dao.UserMapper;
import com.qianxu.mall.model.pojo.User;
import com.qianxu.mall.service.UserService;
import com.qianxu.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/5 21:52
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    /**
     * 获取用户
     *
     * @return
     */
    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    /**
     * 注册用户
     */
    @Override
    public void register(String userName, String password) throws QianxuMallException {
        //查询用户名是否存在, 不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NAME_EXIST);
        }
        //写到数据库
        User user = new User();
        user.setUsername(userName);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new QianxuMallException(QianxuMallExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     */
    @Override
    public User login(String userName, String password) throws QianxuMallException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    /**
     * 更新用户个性签名
     * @param user 用户信息
     * @throws QianxuMallException
     */
    @Override
    public void updateInformation(User user) throws QianxuMallException {
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            throw new QianxuMallException(QianxuMallExceptionEnum.UPDATE_FAILED);
        }
    }



    /**
     * 检查用户权限
     *
     * @param user 用户
     * @return
     */
    @Override
    public boolean checkAdminRole(User user) {
        //1-普通用户, 2-管理员
        return user.getRole().equals(2);
    }
}
