package com.qianxu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.qianxu.mall.common.ApiRestResponse;
import com.qianxu.mall.common.Constant;
import com.qianxu.mall.exception.QianxuMallExceptionEnum;
import com.qianxu.mall.model.pojo.Category;
import com.qianxu.mall.model.pojo.User;
import com.qianxu.mall.model.request.AddCategoryReq;
import com.qianxu.mall.model.request.UpdateCategoryReq;
import com.qianxu.mall.model.vo.CategoryVO;
import com.qianxu.mall.service.CategoryService;
import com.qianxu.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/7 15:07
 * @describe 目录分类Controller
 */
@Controller
public class CategoryController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation("后台添加目录")
    @PostMapping({"/admin/category/add"})
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session,
                                       @Valid @RequestBody AddCategoryReq addCategoryReq) {
        User currentUser = (User) session.getAttribute(Constant.QIANXU_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_LOGIN);
        }
        if (userService.checkAdminRole(currentUser)) {
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("后台更新目录")
    @PutMapping({"/admin/category/update"})
    @ResponseBody
    public ApiRestResponse updateCategory(HttpSession session,
                                          @Valid @RequestBody UpdateCategoryReq updateCategoryReq) {
        User currentUser = (User) session.getAttribute(Constant.QIANXU_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_LOGIN);
        }
        if (userService.checkAdminRole(currentUser)) {
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq, category);
            categoryService.update(category);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(QianxuMallExceptionEnum.NEED_ADMIN);
        }
    }

    @ApiOperation("后台删除目录")
    @DeleteMapping({"/admin/category/delete"})
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id) {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台目录列表")
    @GetMapping({"/admin/category/list"})
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台目录列表")
    @GetMapping({"/category/list"})
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer() {
        List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOList);
    }
}
