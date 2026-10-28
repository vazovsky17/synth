package app.synth.city.api

import app.synth.city.domain.generation.CityGenerationConfig
import app.synth.city.domain.generation.CityGenerator
import app.synth.city.domain.generation.CityStatsCalculator
import app.synth.city.domain.model.City
import app.synth.city.dto.CityResponse
import app.synth.city.dto.CreateCityRequest
import app.synth.city.service.CityService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/cities")
class CityController(
    private val cityService: CityService
) {

    @GetMapping("/generate")
    fun generate(
        @RequestParam(defaultValue = "Synth City") name: String,
        @RequestParam width: Int,
        @RequestParam height: Int,
        @RequestParam seed: Long,
        @RequestParam(defaultValue = "4") roadSpacing: Int,
        @RequestParam(defaultValue = "25") industrialZoneSize: Int,
        @RequestParam(defaultValue = "2") commercialRadius: Int
    ): City {
        val cityStatsCalculator = CityStatsCalculator()
        val config = CityGenerationConfig(
            roadSpacing = roadSpacing,
            industrialZoneSize = industrialZoneSize,
            commercialRadius = commercialRadius
        )

        val tiles = CityGenerator.generate(width, height, seed, config)

        return City(
            name = name,
            seed = seed,
            width = width,
            height = height,
            stats = cityStatsCalculator.calculate(tiles),
            tiles = tiles,
        )
    }

    @PostMapping
    fun createCity(
        @Valid @RequestBody request: CreateCityRequest
    ): ResponseEntity<Map<String, Long>> {
        val config = CityGenerationConfig(
            roadSpacing = request.roadSpacing,
            industrialZoneSize = request.industrialZoneSize,
            commercialRadius = request.commercialRadius
        )

        val id = cityService.createCity(
            name = request.name,
            width = request.width,
            height = request.height,
            seed = request.seed,
            config = config
        )

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapOf("id" to id))
    }

    @GetMapping
    fun getAllCities(): List<CityResponse> {
        return cityService.getAllCities()
    }

    @GetMapping("/{id}")
    fun getCity(@PathVariable id: Long): CityResponse {
        return cityService.getCity(id)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCity(@PathVariable id: Long) {
        cityService.deleteCity(id)
    }
}