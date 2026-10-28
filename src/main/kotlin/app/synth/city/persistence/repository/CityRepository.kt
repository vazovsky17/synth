package app.synth.city.persistence.repository

import app.synth.city.persistence.entity.CityEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CityRepository : JpaRepository<CityEntity, Long>