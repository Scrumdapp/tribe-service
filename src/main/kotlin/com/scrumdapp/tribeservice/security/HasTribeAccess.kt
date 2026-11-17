package com.scrumdapp.tribeservice.security

import com.scrumdapp.tribeservice.tribes.TribeGroupRepository
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TribeAccessAnnotation (
    val paramName: String = ""
)

@Component
class TribeAccessInterceptor(
    private val tribeGroupRepository: TribeGroupRepository
): HandlerInterceptor {
    
}