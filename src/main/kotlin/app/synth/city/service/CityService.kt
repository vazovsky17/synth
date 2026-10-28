package app.synth.city.service

import app.synth.city.domain.generation.CityGenerationConfig
import app.synth.city.domain.generation.CityGenerator
import app.synth.city.domain.model.Tile
import app.synth.city.dto.CityResponse
import app.synth.city.persistence.entity.CityEntity
import app.synth.city.persistence.repository.CityRepository
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import tools.jackson.module.kotlin.readValue

@Service
class CityService(
    private val cityRepository: CityRepository,
    private val objectMapper: ObjectMapper
) {
    fun createCity(
        name: String,
        width: Int,
        height: Int,
        seed: Long,
        config: CityGenerationConfig = CityGenerationConfig()
    ): Long {
        val tiles = CityGenerator.generate(
            width = width,
            height = height,
            seed = seed,
            config = config
        )
        val tilesJson = objectMapper.writeValueAsString(tiles)

        val entity = CityEntity(
            name = name,
            seed = seed,
            width = width,
            height = height,
            tiles = tilesJson
        )
        val savedCity = cityRepository.save(entity)

        return checkNotNull(savedCity.id) {
            "Saved city must have an ID"
        }
    }

    fun getAllCities(): List<CityResponse> {
        return cityRepository.findAll().map(::toResponse)
    }

    fun getCity(id: Long): CityResponse {
        val entity = cityRepository.findById(id)
            .orElseThrow {
                NoSuchElementException("City with id $id not found")
            }

        return toResponse(entity)
    }


    fun deleteCity(id: Long) {
        if (!cityRepository.existsById(id)) {
            throw NoSuchElementException("City with id $id not found")
        }

        cityRepository.deleteById(id)
    }

    private fun toResponse(entity: CityEntity): CityResponse {
        val tiles: List<Tile> = objectMapper.readValue(entity.tiles)

        return CityResponse(
            id = checkNotNull(entity.id),
            name = entity.name,
            seed = entity.seed,
            width = entity.width,
            height = entity.height,
            tiles = tiles
        )
    }
}