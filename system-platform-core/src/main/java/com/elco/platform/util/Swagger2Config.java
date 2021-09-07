//package com.elco.platform.util;
//
//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Profile;
//import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.service.AuthorizationScope;
//import springfox.documentation.service.SecurityReference;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @author kay
// * @date 2020/9/7
// */
//@Configuration
//@EnableSwagger2
//@EnableKnife4j
//@Import(BeanValidatorPluginsConfiguration.class)
////@ConditionalOnProperty(value = {"knife4j.enable"},matchIfMissing = true)
//@Profile("!pro")
//public class Swagger2Config {
//    @Bean
//    public Docket createRestApi() {
//        return (new Docket(DocumentationType.SWAGGER_2)).enable(true)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
//                .build()
//                .securityContexts(this.securityContexts())
//                .securitySchemes(this.securitySchemes());
//    }
//
//    private List<ApiKey> securitySchemes() {
//        //设置请求头信息
//        List<ApiKey> result = new ArrayList<>();
//        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
//        result.add(apiKey);
//        return result;
//    }
//
//    private List<SecurityContext> securityContexts() {
//        //设置需要登录认证的路径
//        List<SecurityContext> result = new ArrayList<>();
//        result.add(getContextByPath("/misty/.*"));
//        return result;
//    }
//
//    private SecurityContext getContextByPath(String pathRegex){
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex(pathRegex))
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        List<SecurityReference> result = new ArrayList<>();
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        result.add(new SecurityReference("Authorization", authorizationScopes));
//        return result;
//    }
//
//private ApiInfo apiInfo(){
//    return new ApiInfoBuilder()
//            .title("系统平台接口")
//            .description("系统平台-k8s")
//            .version("1.2")
//            .build();
//}
//
//
//}
