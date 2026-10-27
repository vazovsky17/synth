package app.synth.city.domain.generation

import app.synth.city.domain.model.CityStats
import app.synth.city.domain.model.Tile
import app.synth.city.domain.model.TileType

class CityStatsCalculator {

    fun calculate(tiles: List<Tile>): CityStats {
        return CityStats(
            roads = tiles.count { it.type == TileType.ROAD },
            residential = tiles.count { it.type == TileType.RESIDENTIAL },
            commercial = tiles.count { it.type == TileType.COMMERCIAL },
            industrial = tiles.count { it.type == TileType.INDUSTRIAL }
        )
    }
}