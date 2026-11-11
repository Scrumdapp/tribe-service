package com.scrumdapp.tribeservice.security

import com.scrumdapp.passportplugin.filters.PassportAuthFilter
import com.scrumdapp.passportplugin.filters.usePassport
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.disable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val passportFilter: PassportAuthFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { disable() }
            .usePassport(passportFilter)

            .authorizeHttpRequests {
                it.requestMatchers("/tribes").hasAuthority("COACH")
                it.requestMatchers("/tribes/{tribeId}").hasAuthority("COACH")
                it.requestMatchers("/tribes/{tribeId}/sessions/*").hasAuthority("COACH")
            }

        return http.build()
    }
}