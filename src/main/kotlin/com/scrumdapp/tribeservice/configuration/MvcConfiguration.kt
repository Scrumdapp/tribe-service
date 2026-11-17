package com.scrumdapp.tribeservice.configuration

import com.scrumdapp.tribeservice.security.TribeAccessInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MvcConfiguration(
    private val tribeAccessInterceptor: TribeAccessInterceptor
): WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tribeAccessInterceptor)
    }
}