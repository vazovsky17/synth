package app.synth.city.domain.generation

import app.synth.city.domain.model.Tile
import app.synth.city.domain.model.TileType
import kotlin.math.abs
import kotlin.random.Random

object CityGenerator {

    fun generate(
        width: Int,
        height: Int,
        seed: Long,
        config: CityGenerationConfig = CityGenerationConfig(),
    ): List<Tile> {

        // Validate city dimensions
        require(width > 0) { "Width must be positive" }
        require(height > 0) { "Height must be positive" }

        // Validate generation configuration
        require(config.roadSpacing > 0) { "Road spacing must be positive" }
        require(config.industrialZoneSize in 1..50) { "Industrial zone size must be between 1 and 50" }
        require(config.commercialRadius >= 0) { "Commercial radius must not be negative" }

        // Initialize deterministic random generator
        val random = Random(seed)
        val tiles = mutableListOf<Tile>()

        // Calculate city center
        val centerX = width / 2
        val centerY = height / 2

        // Select industrial district location
        val industrialCorner = random.nextInt(4)

        // Calculate industrial district boundaries
        val industrialWidth = width * config.industrialZoneSize / 100
        val industrialHeight = height * config.industrialZoneSize / 100

        // Generate city tiles
        for (x in 0 until width) {
            for (y in 0 until height) {

                val distance = abs(x - centerX) + abs(y - centerY)

                val isCityCenter =
                    distance <= config.commercialRadius

                val isIndustrialZone = when (industrialCorner) {
                    // North-West
                    0 -> x < industrialWidth && y < industrialHeight

                    // North-East
                    1 -> x >= width - industrialWidth && y < industrialHeight

                    // South-West
                    2 -> x < industrialWidth && y >= height - industrialHeight

                    // South-East
                    else -> x >= width - industrialWidth && y >= height - industrialHeight
                }

                val type = when {
                    // Roads always have priority
                    x % config.roadSpacing == 0 ||
                            y % config.roadSpacing == 0 ->
                        TileType.ROAD

                    // Commercial city center
                    isCityCenter ->
                        TileType.COMMERCIAL

                    // Industrial district
                    isIndustrialZone ->
                        TileType.INDUSTRIAL

                    // Other buildings
                    else -> TileType.entries
                        .filter { it != TileType.ROAD }
                        .random(random)
                }

                tiles.add(Tile(x = x, y = y, type = type))
            }
        }

        return tiles
    }
}
