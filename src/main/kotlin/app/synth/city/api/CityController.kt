package app.synth.city.api

import app.synth.city.domain.generation.CityGenerator
import app.synth.city.domain.model.Tile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CityController {

    @GetMapping("/api/v1/hello")
    fun hello() = "Welcome to SYNTH!"

    @GetMapping("/api/v1/cities/generate")
    fun generate(
        @RequestParam width: Int,
        @RequestParam height: Int,
        @RequestParam seed: Long
    ): List<Tile> {
        val cityGenerator = CityGenerator()

        val tiles = cityGenerator.generate(width, height, seed)

        return tiles
    }
}