package br.com.Turismo_40.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Turismo 4.0")
                .description("Documentação completa da API de Turismo, com endpoints para gerenciamento de usuários, perfis, atrações, eventos e roteiros personalizados.")
                .version("1.0.0"));
    }
}