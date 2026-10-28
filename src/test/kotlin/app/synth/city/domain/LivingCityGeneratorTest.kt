package app.synth.city.domain

import app.synth.city.domain.generation.CityGenerationConfig
import app.synth.city.domain.model.TileType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LivingCityGeneratorTest {


    @Test
    fun `generates crossing roads in the center`() {
        val width = 5
        val height = 5

        val tiles = LivingCityGenerator.generate(
            width = width,
            height = height,
            seed = 42L,
            config = CityGenerationConfig()
        )

        assertEquals(width * height, tiles.size)

        val centerX = width / 2
        val centerY = height / 2

        // Every tile in the central vertical road must be ROAD.
        for (y in 0 until height) {
            val tile = tiles.first { it.x == centerX && it.y == y }
            assertEquals(TileType.ROAD, tile.type)
        }

        // Every tile in the central horizontal road must be ROAD.
        for (x in 0 until width) {
            val tile = tiles.first { it.x == x && it.y == centerY }
            assertEquals(TileType.ROAD, tile.type)
        }
    }


    @Test
    fun `same seed generates identical city`() {
        val first = LivingCityGenerator.generate(
            width = 30,
            height = 30,
            seed = 42L,
            config = CityGenerationConfig()
        )

        val second = LivingCityGenerator.generate(
            width = 30,
            height = 30,
            seed = 42L,
            config = CityGenerationConfig()
        )

        assertEquals(first, second)
    }

}
