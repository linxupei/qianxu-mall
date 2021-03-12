package com.qianxu.mall.model.dao;

import com.qianxu.mall.model.pojo.Cart;
import com.qianxu.mall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<CartVO> list(Integer userId);

    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    int selectOrNot(@Param("userId") Integer userId,
                    @Param("productId") Integer productId,
                    @Param("selected")  Integer selected);
}