package org.retriever.server.dailypet.global.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig{
    @Bean
    public OpenAPI OpenAPI() {
        return new OpenAPI()
                .info(new Info().title("DailyPet API Docs")
                        .description("API Docs for DailyPet")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("DailyPet Team Repository")
                        .url("https://github.com/SWM-Retriever"));
    }

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(AuthenticationPrincipal.class);
    }
}
