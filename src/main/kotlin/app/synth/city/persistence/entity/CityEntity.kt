package app.synth.city.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.OffsetDateTime

@Entity
@Table(name = "cities")
class CityEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(nullable = false)
    var seed: Long,

    @Column(nullable = false)
    var width: Int,

    @Column(nullable = false)
    var height: Int,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tiles", nullable = false, columnDefinition = "jsonb")
    var tiles: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    constructor() : this(
        id = null,
        name = "",
        seed = 0L,
        width = 0,
        height = 0,
        tiles = "",
        createdAt = OffsetDateTime.now()
    )
}
