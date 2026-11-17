package com.scrumdapp.tribeservice.checkpoints

import com.scrumdapp.tribeservice.tribes.TribeGroupRepository
import org.springframework.stereotype.Service

@Service
class TribeCheckpointService(
    private val tribeGroupRepository: TribeGroupRepository
) {

}