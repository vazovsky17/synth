package app.synth.city.api

import app.synth.city.domain.generation.CityGenerator
import app.synth.city.domain.generation.CityStatsCalculator
import app.synth.city.domain.model.City
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CityController {

    @GetMapping("/api/v1/hello")
    fun hello() = "Welcome to SYNTH!"

    @GetMapping("/api/v1/cities/generate")
    fun generate(
        @RequestParam(defaultValue = "Synth City") name: String,
        @RequestParam width: Int,
        @RequestParam height: Int,
        @RequestParam seed: Long
    ): City {
        val cityGenerator = CityGenerator()
        val cityStatsCalculator = CityStatsCalculator()

        val tiles = cityGenerator.generate(width, height, seed)

        return City(
            name = name,
            seed = seed,
            width = width,
            height = height,
            stats = cityStatsCalculator.calculate(tiles),
            tiles = tiles,
        )
    }
}