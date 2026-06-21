package com.mj.expensetracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI walletManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("荷包管理系统 API")
                        .description("荷包管理系统 - 管理个人财务")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Wallet Team")
                                .email("support@wallet.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}