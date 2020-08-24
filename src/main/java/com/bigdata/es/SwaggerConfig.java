package com.bigdata.es;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


/**
 * swagger2的配置文件，建在项目的启动类的同级目录下,访问路径：http://localhost:8080/swagger-ui.html
 *
 * @Author: spencer.
 * @Description: TODO()
 * @Date:Created in 2018/9/1.
 * @Modified By:
 */
@Configuration
@EnableSwagger2
//@ComponentScan(basePackages = "com.bigdata.wb.controller")
public class SwaggerConfig
{
    @Bean
    public Docket createdRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            //RequestHandlerSelectors配置要扫描接口的方式
            .apis(RequestHandlerSelectors.basePackage("com.bigdata.es.controller"))
            .paths(PathSelectors.any())
            .build();
    }

//    private ApiInfo apiInfo()
//    {
//        return new ApiInfoBuilder()
//            //页面标题
//            .title("接口测试文档")
//            //创建人
//            .contact(new Contact("spencer", "", ""))
//            //版本号
//            .version("1.0.0")
//            //描述
//            .description("API文档描述")
//            .build();
//    }

    private ApiInfo apiInfo(){

        Contact contact = new Contact("spencer", "", "spencer_spark@sina.com");

        return new ApiInfo(
                "Spencer的API接口文档",
                "Anything is possible",
                "v1.0",
                "",
                contact,
                "Apache 2.0",
                "",
                new ArrayList<>()
        );
    }
}
