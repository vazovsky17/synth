package app.synth.city.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun synthOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("SYNTH // Procedural City Engine")
                    .version("1.0.0")
                    .description(
                        """
                        REST API for deterministic procedural city generation.

                        Features:
                        - Seed-based city generation
                        - Configurable roads and zoning
                        - Commercial and industrial districts
                        - PostgreSQL persistence
                        - City CRUD operations
                        """.trimIndent()
                    )
            )
    }
}