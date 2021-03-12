package com.qianxu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.qianxu.mall.common.ApiRestResponse;
import com.qianxu.mall.common.Constant;
import com.qianxu.mall.exception.QianxuMallException;
import com.qianxu.mall.exception.QianxuMallExceptionEnum;
import com.qianxu.mall.model.pojo.Product;
import com.qianxu.mall.model.request.AddProductReq;
import com.qianxu.mall.model.request.UpdateProductReq;
import com.qianxu.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/11 16:11
 * @describe 后台商品controller
 */
@RestController
public class ProductAdminController {
    @Autowired
    private ProductService productService;

    @ApiOperation("添加商品")
    @PostMapping({"/admin/product/add"})
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @ApiOperation("上传文件")
    @PostMapping({"admin/upload/file"})
    public ApiRestResponse upload(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        //创建文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new QianxuMallException(QianxuMallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL() + "")) +
                    "/images/" + newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.UPDATE_FAILED);
        }
    }

    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                    null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    @ApiOperation("更新商品")
    @PutMapping({"/admin/product/update"})
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    @ApiOperation("删除商品")
    @DeleteMapping({"/admin/product/delete"})
    public ApiRestResponse deleteProduct(@RequestParam Integer id) {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量上下架接口")
    @PutMapping({"/admin/product/batchUpdateSellStatus"})
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,
                                                 @RequestParam Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品列表")
    @GetMapping({"/admin/product/list"})
    public ApiRestResponse list(@RequestParam Integer pageNum,
                                                 @RequestParam Integer pageSize) {
        PageInfo pageInfo = productService.selectListForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }
}
