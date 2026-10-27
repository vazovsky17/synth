package app.synth.city.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CityController {

    @GetMapping("/api/v1/hello")
    fun hello() = "Welcome to SYNTH!"

}