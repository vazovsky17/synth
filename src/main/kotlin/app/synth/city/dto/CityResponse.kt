package app.synth.city.dto

import app.synth.city.domain.model.Tile

data class CityResponse(
    val id: Long,
    val name: String,
    val seed: Long,
    val width: Int,
    val height: Int,
    val tiles: List<Tile>
)