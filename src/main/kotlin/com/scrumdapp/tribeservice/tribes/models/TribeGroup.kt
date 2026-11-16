package com.scrumdapp.tribeservice.tribes.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tribe_groups")
class TribeGroup {

    @Id
    var groupId: Long = 0

    @ManyToOne
    @JoinColumn(name = "tribe_id", nullable = false)
    var tribe: Tribe = Tribe()
}