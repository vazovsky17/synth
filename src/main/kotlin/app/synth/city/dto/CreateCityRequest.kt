package app.synth.city.dto

data class CreateCityRequest(
    val name: String,
    val width: Int,
    val height: Int,
    val seed: Long
)