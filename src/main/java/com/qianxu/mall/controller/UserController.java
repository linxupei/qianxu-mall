package com.qianxu.mall.controller;

import com.qianxu.mall.common.ApiRestResponse;
import com.qianxu.mall.common.Constant;
import com.qianxu.mall.exception.QianxuMallException;
import com.qianxu.mall.exception.QianxuMallExceptionEnum;
import com.qianxu.mall.model.pojo.User;
import com.qianxu.mall.service.UserService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/5 21:50
 *
 * 用户控制器
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping({"/personal_page"})
    @ResponseBody
    public User personalPage() {
        return userService.getUser();
    }

    @PostMapping({"/register"})
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password) throws QianxuMallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不少于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName, password);
        return ApiRestResponse.success();
    }

    @PostMapping({"/login"})
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password,
                                 HttpSession session) throws QianxuMallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        user.setPassword(null);
        session.setAttribute(Constant.QIANXU_MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    @PostMapping({"/user/update"})
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature) throws QianxuMallException {
        User currentUser = (User) session.getAttribute(Constant.QIANXU_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonailzedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    @PostMapping({"/user/logout"})
    @ResponseBody
    public ApiRestResponse Logout(HttpSession session) {
        session.removeAttribute(Constant.QIANXU_MALL_USER);
        return ApiRestResponse.success();
    }

    @PostMapping({"/adminLogin"})
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password,
                                 HttpSession session) throws QianxuMallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        if (userService.checkAdminRole(user)) {
            user.setPassword(null);
            session.setAttribute(Constant.QIANXU_MALL_USER, user);
        } else {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_ADMIN);
        }
        return ApiRestResponse.success(user);
    }
}
