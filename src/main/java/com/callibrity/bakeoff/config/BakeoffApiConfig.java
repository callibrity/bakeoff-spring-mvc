package com.callibrity.bakeoff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BakeoffApiConfig {

    @Bean
    public OpenAPI bakeoffOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Bakeoff API")
                        .description("Simple REST API")
                        .version("1.0"));
    }

}
