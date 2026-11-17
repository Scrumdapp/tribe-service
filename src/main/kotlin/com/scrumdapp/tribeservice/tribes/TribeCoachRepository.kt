package com.scrumdapp.tribeservice.tribes

import com.scrumdapp.tribeservice.tribes.models.TribeCoach
import com.scrumdapp.tribeservice.tribes.models.TribeCoachId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TribeCoachRepository: JpaRepository<TribeCoach, TribeCoachId> {

    fun existsByUserIdAndTribeId(userId: Long, tribeId: Long): Boolean
}