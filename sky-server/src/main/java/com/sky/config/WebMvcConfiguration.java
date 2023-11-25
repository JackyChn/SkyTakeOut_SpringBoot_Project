package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * Register self-defined interceptor
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Starting to register interceptor...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("user/shop/status");
    }

    /**
     *  generate Swagger for admin via knife4j
     * @return
     */
    @Bean
    public Docket docket1() {
        log.info("Preparing to generate Interface file...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("SkyTakeout Interface")
                .version("2.0")
                .description("SkyTakeout Interface")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("Admin Interface")
                .apiInfo(apiInfo)
                .select()
                // declare the package that needed to scan
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     *  generate Swagger for user via knife4j
     * @return
     */
    @Bean
    public Docket docket2() {
        log.info("Preparing to generate Interface file...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("SkyTakeout Interface")
                .version("2.0")
                .description("SkyTakeout Interface")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("User Interface")
                .apiInfo(apiInfo)
                .select()
                // declare the package that needed to scan
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * set static map
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Preparing to set static resource mapping...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
