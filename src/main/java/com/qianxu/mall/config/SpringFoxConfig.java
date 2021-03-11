package com.qianxu.mall.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/7 16:39
 * @describe springfox-swagger2 API文档生成工具配置
 */
@Configuration
public class SpringFoxConfig {
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("谦虚生鲜")
                .description("")
                .termsOfServiceUrl("")
                .build();
    }

}
