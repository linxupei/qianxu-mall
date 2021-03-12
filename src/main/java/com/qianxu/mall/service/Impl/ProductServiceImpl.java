package com.qianxu.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianxu.mall.common.Constant;
import com.qianxu.mall.exception.QianxuMallException;
import com.qianxu.mall.exception.QianxuMallExceptionEnum;
import com.qianxu.mall.model.dao.ProductMapper;
import com.qianxu.mall.model.pojo.Product;
import com.qianxu.mall.model.query.ProductListQuery;
import com.qianxu.mall.model.request.AddProductReq;
import com.qianxu.mall.model.request.ProductListReq;
import com.qianxu.mall.model.vo.CategoryVO;
import com.qianxu.mall.service.CategoryService;
import com.qianxu.mall.service.ProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/11 16:16
 * @describe 商品服务实现类
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void add(AddProductReq addProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        //查询是否重名
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NAME_EXIST);
        }
        //判断是否添加成功
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new QianxuMallException(QianxuMallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(Product updateProduct) {
        Product productOld = productMapper.selectByName(updateProduct.getName());
        //同名且不同id, 不能继续修改
        if (productOld != null && !productOld.getId().equals(updateProduct.getId())) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NAME_EXIST);
        }
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count == 0) {
            throw new QianxuMallException(QianxuMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new QianxuMallException(QianxuMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        int count = productMapper.batchUpdateSellStatus(ids, sellStatus);
        if (count == 0) {
            throw new QianxuMallException(QianxuMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public PageInfo selectListForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq) {
        //构建ProductListQuery对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索处理
        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            String keyword = new StringBuilder().append("%").
                    append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(keyword);
        }

        //目录处理: 如果查某个目录下的商品, 不仅是需要查出该目录下的, 还要把所有子目录的所有商品都查出来,
        //所以要拿到一个目录id的List
        if (productListReq.getCategoryId() != null) {
            List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            List<Integer> categoryIds = new ArrayList<>();
            getCategoryIds(categoryVOList, categoryIds);
            if (categoryIds.size() > 0) {
                productListQuery.setCategoryIds(categoryIds);
            }
        }

        //排序处理
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }

        List<Product> products = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    /**
     * 把所有目录id找出来
     */
    private void getCategoryIds(List<CategoryVO> categoryVOList, List<Integer> categoryIds) {
        for (CategoryVO categoryVO : categoryVOList) {
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }
}
