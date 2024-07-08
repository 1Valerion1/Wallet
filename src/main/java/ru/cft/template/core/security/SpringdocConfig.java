package ru.cft.template.core.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        security = @SecurityRequirement(name = "X-Session-Token")
)
@Configuration
@SecurityScheme(
        name = "X-Session-Token",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-Session-Token"
)
public class SpringdocConfig {
    //http://localhost:8081/swagger-ui/index.html
    private final String DOC_TITLE = "Документация по кошельку картошка ";
    private final String DOC_VERSION = "2.0.0";
    private final String DOC_DESCRIPTION = "Картошка - это отечественный электронный кошелек, который " +
            "предоставляет возможность оплаты услуг и осуществления денежных переводов в рамках своей системы." +
            " В данном документе содержится описание основной логики взаимодействия с системой Картошка, включая создание пользователя," +
            " получение информации о кошельке, создание счетов, оплату, отмену операций и получение информации о счетах.";

    @Bean
    public OpenAPI baseOpenApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(DOC_TITLE)
                                .version(DOC_VERSION)
                                .description(DOC_DESCRIPTION)
                );
    }
}