package com.qianxu.mall.controller;

import com.qianxu.mall.common.ApiRestResponse;
import com.qianxu.mall.filter.UserFilter;
import com.qianxu.mall.model.vo.CartVO;
import com.qianxu.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/12 15:18
 * @describe
 */
@RestController
@RequestMapping({"/cart"})
public class CartController {
    @Autowired
    private CartService cartService;

    @ApiOperation("获取用户列表")
    @GetMapping({"/list"})
    public ApiRestResponse list() {
        //内部获取用户id, 防止横向越权
        List<CartVO> cartVOS = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("添加商品到购物车, 或改变商品数量")
    @PostMapping({"/add"})
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        List<CartVO> cartVOS = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("更新购物车商品数量")
    @PutMapping({"/update"})
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam @Min(1) Integer count) {
        List<CartVO> cartVOS = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("删除购物车商品")
    @DeleteMapping({"/delete"})
    public ApiRestResponse delete(@RequestParam Integer productId) {
        List<CartVO> cartVOS = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("选择/不选择购物车某商品")
    @PutMapping({"/select"})
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
        List<CartVO> cartVOS = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("全选择/全不选择购物车某商品")
    @PutMapping({"/selectAll"})
    public ApiRestResponse selectAll(@RequestParam Integer selected) {
        List<CartVO> cartVOS = cartService.selectAllOrNot(UserFilter.currentUser.getId(), selected);
        return ApiRestResponse.success(cartVOS);
    }
}
