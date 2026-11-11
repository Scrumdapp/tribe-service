package com.scrumdapp.tribeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TribeServiceApplication

fun main(args: Array<String>) {
    runApplication<TribeServiceApplication>(*args)
}
