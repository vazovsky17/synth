package app.synth.city.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCityRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val name: String,

    @field:Min(1)
    @field:Max(100)
    val width: Int,

    @field:Min(1)
    @field:Max(100)
    val height: Int,

    val seed: Long,

    @field:Min(1)
    val roadSpacing: Int = 4,

    @field:Min(1)
    @field:Max(50)
    val industrialZoneSize: Int = 25,

    @field:Min(0)
    val commercialRadius: Int = 2
)