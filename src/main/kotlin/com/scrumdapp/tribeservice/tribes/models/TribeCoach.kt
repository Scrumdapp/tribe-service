package com.scrumdapp.tribeservice.tribes.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tribe_coaches")
@IdClass(TribeCoachId::class)
class TribeCoach {
    @Id
    @ManyToOne
    @JoinColumn(name = "tribe_id")
    var tribe: Tribe? = null

    @Id
    var userId: Long? = null
}