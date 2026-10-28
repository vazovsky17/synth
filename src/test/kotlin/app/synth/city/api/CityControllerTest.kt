package app.synth.city.api

import app.synth.city.service.CityService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(CityController::class)
class CityControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    @MockitoBean
    lateinit var cityService: CityService

    @Test
    fun `invalid width returns bad request`() {
        mockMvc.get("/api/v1/cities/generate") {
            param("width", "0")
            param("height", "8")
            param("seed", "42")
        }.andDo {
            print()
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.error") { value("Bad Request") }
            jsonPath("$.message") { value("Width must be positive") }
        }
    }
}