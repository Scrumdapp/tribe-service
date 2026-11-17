package com.scrumdapp.tribeservice.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

@Component
class DownstreamRequestClient(
    @Value($$"${spring.application.name}") private val appName: String,
    private val client: RestClient = RestClient.create(),
    private val objectMapper: ObjectMapper = ObjectMapper()
) {

    private fun getAuthHeader(): String {

        val jwt = SecurityContextHolder.getContext().authentication as Jwt
        return "bearer ${jwt.tokenValue}"
    }

    final inline fun <reified T> performRequest(method: HttpMethod, uri: String, body: Any? = null): T {
        return performRequestInternal(method, uri, body, object : TypeReference<T>() {})
    }

    @PublishedApi
    internal fun <T> performRequestInternal(method: HttpMethod, uri: String, body: Any? = null, typeRef: TypeReference<T>): T {

        try {
            val res = client.method(method)
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
                .header(HttpHeaders.VIA, appName)
                .accept(MediaType.APPLICATION_JSON)
                .body(body ?: "")
                .retrieve()
                .toEntity<String>()
            return objectMapper.readValue(res.body, typeRef)
        } catch (e: Exception) {
            throw RuntimeException("replace this with downstream exception", e)
        }
    }
}