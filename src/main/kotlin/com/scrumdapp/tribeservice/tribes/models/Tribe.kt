package com.scrumdapp.tribeservice.tribes.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tribe")
class Tribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    var ownerId: Long = 0

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = true)
    var description: String? = null

    @Column(nullable = true)
    var backgroundPreference: String? = null

    var isActive: Boolean = true
}