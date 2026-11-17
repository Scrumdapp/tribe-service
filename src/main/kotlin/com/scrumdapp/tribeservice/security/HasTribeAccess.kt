package com.scrumdapp.tribeservice.security

import com.scrumdapp.passportplugin.jwt.PassportContent
import com.scrumdapp.passportplugin.utils.getTemplateVariables
import com.scrumdapp.tribeservice.tribes.TribeCoachRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class HasTribeAccess (
    val paramName: String = "",
)

@Component
class TribeAccessInterceptor(
    private val tribeCoachRepository: TribeCoachRepository
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) return true

        val annotation = handler.getMethodAnnotation(HasTribeAccess::class.java) ?: return true

        val pathVariables = request.getTemplateVariables()
        val tribeId = pathVariables[annotation.paramName.ifEmpty { "tribeId" }]?.toLongOrNull()
            ?: throw IllegalStateException("Could not find tribeId parameter in request")

        val userId = PassportContent.fromStaticSecurityContext().userId

        if (!tribeCoachRepository.existsByUserIdAndTribeId(userId, tribeId)) throw AccessDeniedException("No access")
        return true
    }
}