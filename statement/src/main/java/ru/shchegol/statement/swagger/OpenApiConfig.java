package ru.shchegol.statement.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Statement Service",
                contact = @Contact(
                        name = "Shchegolev Ilya",
                        url = "https://github.com/lilpumpy19"
                )
        )
)
public class OpenApiConfig {
}
