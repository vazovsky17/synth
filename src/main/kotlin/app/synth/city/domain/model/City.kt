package app.synth.city.domain.model

data class City(
    val name: String,
    val seed: Long,
    val width: Int,
    val height: Int,
    val tiles: List<Tile>,
)