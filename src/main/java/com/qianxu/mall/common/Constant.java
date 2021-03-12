package com.qianxu.mall.common;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/6 19:54
 * @describe
 */
@Component
public class Constant {
    public static final String QIANXU_MALL_USER = "qianxu_mall_user";

    public static final String SALT = "1/da1@&^s7fy2.,,opfkgs[;";

    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    public interface SaleStatus {
        //商品上架状态
        int SALE = 1;
        //商品下架状态
        int NOT_SALE = 0;
    }

    public interface CheckStatus {
        //商品选中状态
        int CHECKED = 1;
        //商品未选中状态
        int UN_CHECKED = 0;
    }
}
