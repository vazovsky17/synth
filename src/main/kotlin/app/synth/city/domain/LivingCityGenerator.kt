package app.synth.city.domain

import app.synth.city.domain.generation.CityGenerationConfig
import app.synth.city.domain.model.Tile
import app.synth.city.domain.model.TileType
import kotlin.random.Random

object LivingCityGenerator : CityGenerationStrategy {

    override fun generate(
        width: Int,
        height: Int,
        seed: Long,
        config: CityGenerationConfig
    ): List<Tile> {
        require(width > 0) { "Width must be positive" }
        require(height > 0) { "Height must be positive" }
        require(config.roadSpacing > 0) {
            "Road spacing must be positive"
        }

        val random = Random(seed)
        val centerX = width / 2
        val centerY = height / 2

        val verticalRoads = mutableSetOf(centerX)
        val horizontalRoads = mutableSetOf(centerY)

        for (x in 0 until width step config.roadSpacing) {
            val offset = random.nextInt(-1, 2)
            verticalRoads.add(
                (x + offset).coerceIn(0, width - 1)
            )
        }

        for (y in 0 until height step config.roadSpacing) {
            val offset = random.nextInt(-1, 2)
            horizontalRoads.add(
                (y + offset).coerceIn(0, height - 1)
            )
        }

        return buildList {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val isRoad =
                        x in verticalRoads ||
                                y in horizontalRoads

                    val type = if (isRoad) {
                        TileType.ROAD
                    } else {
                        TileType.RESIDENTIAL
                    }

                    add(Tile(x = x, y = y, type = type))
                }
            }
        }
    }
}
