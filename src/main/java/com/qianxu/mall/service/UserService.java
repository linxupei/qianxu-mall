package com.qianxu.mall.service;

import com.qianxu.mall.exception.QianxuMallException;
import com.qianxu.mall.model.pojo.User;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/5 21:51
 */
public interface UserService {
    /**
     * 获取用户
     * @return
     */
    public User getUser();

    /**
     * 注册用户
     */
    public void register(String userName, String password) throws QianxuMallException;

    /**
     * 用户登录
     */
    public User login(String userName, String password) throws QianxuMallException;

    /**
     * 更新用户个性签名
     * @param user 用户信息
     * @throws QianxuMallException
     */
    public void updateInformation(User user) throws QianxuMallException;

    /**
     * 检查用户权限
     * @param user 用户
     * @return
     */
    public boolean checkAdminRole(User user);
}
