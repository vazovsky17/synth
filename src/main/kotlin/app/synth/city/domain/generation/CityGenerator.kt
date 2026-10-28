package app.synth.city.domain.generation

import app.synth.city.domain.model.Tile
import app.synth.city.domain.model.TileType
import kotlin.random.Random

object CityGenerator {

    fun generate(width: Int, height: Int, seed: Long): List<Tile> {
        require(width > 0) {
            "Width must be positive"
        }
        require(height > 0) {
            "Height must be positive"
        }

        val random = Random(seed)
        val tiles = mutableListOf<Tile>()

        val centerX = width / 2
        val centerY = height / 2

        for (x in 0 until width) {
            for (y in 0 until height) {
                val distance = kotlin.math.abs(x - centerX) +
                        kotlin.math.abs(y - centerY)

                val isCityCenter = distance <= 2
                val type = when {
                    x % 4 == 0 || y % 4 == 0 -> TileType.ROAD

                    isCityCenter -> TileType.COMMERCIAL

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