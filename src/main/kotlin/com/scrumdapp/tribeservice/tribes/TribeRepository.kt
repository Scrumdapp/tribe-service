package com.scrumdapp.tribeservice.tribes

import com.scrumdapp.tribeservice.tribes.models.Tribe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TribeRepository: JpaRepository<Tribe, Long> {

    fun getTribeById(id: Long): Tribe?
}

