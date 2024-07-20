package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }


    public Info apiInfo() {
        return new Info()
                .title("Whataplabs API - PRODUCT")
                .description("상품 조회, 등록, 수정, 삭제에 대한 API 입니다.")
                .version("0.0.1");
    }
}
