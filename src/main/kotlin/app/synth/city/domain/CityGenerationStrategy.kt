package app.synth.city.domain

import app.synth.city.domain.generation.CityGenerationConfig
import app.synth.city.domain.model.Tile

interface CityGenerationStrategy {
    fun generate(
        width: Int,
        height: Int,
        seed: Long,
        config: CityGenerationConfig
    ): List<Tile>
}