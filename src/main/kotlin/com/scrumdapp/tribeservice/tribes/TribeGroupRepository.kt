package com.scrumdapp.tribeservice.tribes

import com.scrumdapp.tribeservice.tribes.models.TribeGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TribeGroupRepository : JpaRepository<TribeGroup, Long> {

    @Query("""
        SELECT tg
        FROM TribeGroup tg
        WHERE EXISTS (
            SELECT tc
            FROM TribeCoach tc
            WHERE tc.tribe = tg.tribe
            AND tc.userId = :userId
        )
    """)
    fun findByUserId(@Param("userId") userId: Long): List<TribeGroup>

    fun findByTribeId(tribeId: Long): List<TribeGroup>
}

