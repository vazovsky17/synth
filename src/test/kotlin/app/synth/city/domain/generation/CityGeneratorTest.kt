package app.synth.city.domain.generation

import app.synth.city.domain.model.TileType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

class CityGeneratorTest {

    @Test
    fun `same seed produces identical cities`() {
        val cityGenerator = CityGenerator()

        val firstGeneration = cityGenerator.generate(10, 10, 42L)
        val secondGeneration = cityGenerator.generate(10, 10, 42L)

        assertEquals(firstGeneration, secondGeneration)
    }

    @Test
    fun `zero width throws exception`() {
        val cityGenerator = CityGenerator()

        assertThrows<IllegalArgumentException> {
            cityGenerator.generate(0, 10, 42L)
        }
    }

    @Test
    fun `roads are generated at correct coordinates`() {
        val cityGenerator = CityGenerator()

        val tiles = cityGenerator.generate(10, 10, 42L)
        val tile = tiles.first { it.x == 4 && it.y == 7 }

        assertEquals(TileType.ROAD, tile.type)
    }

    @Test
    fun `buildings are not generated as roads`() {
        val cityGenerator = CityGenerator()

        val tiles = cityGenerator.generate(10, 10, 42L)
        val tile = tiles.first { it.x == 2 && it.y == 3 }

        assertNotEquals(TileType.ROAD, tile.type)
    }

    @Test
    fun `all road tiles follow grid rule`() {
        val cityGenerator = CityGenerator()

        val tiles = cityGenerator.generate(10, 10, 42L).filter { it.type == TileType.ROAD }

        assertTrue(tiles.all { it.x % 4 == 0 || it.y % 4 == 0 })
    }

    @Test
    fun `generated city has correct tile count`() {
        val cityGenerator = CityGenerator()

        val tiles = cityGenerator.generate(10, 8, 42L)

        assertEquals(10 * 8, tiles.size)
    }
}