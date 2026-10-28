package app.synth.city.domain.generation

import app.synth.city.domain.model.TileType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

class CityGeneratorTest {

    // 1. Basic generation

    @Test
    fun `generated city has correct tile count`() {
        val tiles = CityGenerator.generate(10, 8, 42L)

        assertEquals(80, tiles.size)
    }

    @Test
    fun `same seed produces identical cities`() {
        val first = CityGenerator.generate(10, 10, 42L)
        val second = CityGenerator.generate(10, 10, 42L)

        assertEquals(first, second)
    }

    @Test
    fun `different seeds generate different cities`() {
        val first = CityGenerator.generate(20, 20, 42L)
        val second = CityGenerator.generate(20, 20, 123L)

        assertNotEquals(first, second)
    }

    // 2. Road generation

    @Test
    fun `roads are generated at correct coordinates`() {
        val tiles = CityGenerator.generate(10, 10, 42L)
        val tile = tiles.first { it.x == 4 && it.y == 7 }

        assertEquals(TileType.ROAD, tile.type)
    }

    @Test
    fun `buildings are not generated as roads`() {
        val tiles = CityGenerator.generate(10, 10, 42L)
        val tile = tiles.first { it.x == 2 && it.y == 3 }

        assertNotEquals(TileType.ROAD, tile.type)
    }

    @Test
    fun `all road tiles follow grid rule`() {
        val roads = CityGenerator.generate(10, 10, 42L)
            .filter { it.type == TileType.ROAD }

        assertTrue(roads.all { it.x % 4 == 0 || it.y % 4 == 0 })
    }

    // 3. Commercial center

    @Test
    fun `city center contains commercial buildings`() {
        val tiles = CityGenerator.generate(10, 10, 42L)
        val centerTile = tiles.first { it.x == 5 && it.y == 5 }

        assertEquals(TileType.COMMERCIAL, centerTile.type)
    }

    @Test
    fun `roads have priority over commercial center`() {
        val tiles = CityGenerator.generate(10, 10, 42L)
        val tile = tiles.first { it.x == 4 && it.y == 5 }

        assertEquals(TileType.ROAD, tile.type)
    }

    // 4. Industrial districts

    @Test
    fun `industrial zone is generated in one of four corners`() {
        val tiles = CityGenerator.generate(20, 20, 42L)

        val corners = listOf(
            (0 until 5) to (0 until 5),
            (15 until 20) to (0 until 5),
            (0 until 5) to (15 until 20),
            (15 until 20) to (15 until 20)
        )

        val hasIndustrialCorner = corners.any { (xs, ys) ->
            tiles.any { tile ->
                tile.x in xs &&
                        tile.y in ys &&
                        tile.type == TileType.INDUSTRIAL
            }
        }

        assertTrue(hasIndustrialCorner)
    }

    @Test
    fun `roads have priority over industrial zones`() {
        val tiles = CityGenerator.generate(20, 20, 42L)
        val tile = tiles.first { it.x == 16 && it.y == 17 }

        assertEquals(TileType.ROAD, tile.type)
    }

    // 5. Input validation

    @Test
    fun `zero width throws exception`() {
        assertThrows<IllegalArgumentException> {
            CityGenerator.generate(0, 10, 42L)
        }
    }

    // 6. Custom generation configuration

    @Test
    fun `custom road spacing changes road layout`() {
        val config = CityGenerationConfig(roadSpacing = 6)

        val tiles = CityGenerator.generate(12, 12, 42L, config)

        val road = tiles.first { it.x == 6 && it.y == 7 }
        val building = tiles.first { it.x == 4 && it.y == 7 }

        assertEquals(TileType.ROAD, road.type)
        assertNotEquals(TileType.ROAD, building.type)
    }

    @Test
    fun `custom commercial radius expands city center`() {
        val config = CityGenerationConfig(commercialRadius = 4)

        val tiles = CityGenerator.generate(20, 20, 42L, config)

        val tile = tiles.first { it.x == 11 && it.y == 13 }

        assertEquals(TileType.COMMERCIAL, tile.type)
    }

    @Test
    fun `larger industrial zone generates more industrial tiles`() {
        val small = CityGenerator.generate(
            width = 40,
            height = 40,
            seed = 42L,
            config = CityGenerationConfig(industrialZoneSize = 10)
        )

        val large = CityGenerator.generate(
            width = 40,
            height = 40,
            seed = 42L,
            config = CityGenerationConfig(industrialZoneSize = 40)
        )

        val smallCount = small.count { it.type == TileType.INDUSTRIAL }
        val largeCount = large.count { it.type == TileType.INDUSTRIAL }

        assertTrue(largeCount > smallCount)
    }
}
