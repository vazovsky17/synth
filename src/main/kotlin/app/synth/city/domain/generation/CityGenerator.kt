package app.synth.city.domain.generation

import app.synth.city.domain.model.Tile
import app.synth.city.domain.model.TileType
import kotlin.random.Random

class CityGenerator {

    fun generate(width: Int, height: Int, seed: Long): List<Tile> {
        require(width > 0) {
            "Width must be positive"
        }
        require(height > 0) {
            "Height must be positive"
        }

        val random = Random(seed)

        val tiles = mutableListOf<Tile>()

        for (x in 0 until width) {
            for (y in 0 until height) {
                tiles.add(
                    Tile(
                        x = x,
                        y = y,
                        type = if (x % 4 == 0 || y % 4 == 0) {
                            TileType.ROAD
                        } else {
                            TileType.entries
                                .filter { it != TileType.ROAD }
                                .random(random)
                        }
                    )
                )
            }
        }


        return tiles
    }
}