package app.synth.city.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(exception: IllegalArgumentException): ResponseEntity<ApiErrorResponse> {
        val response = ApiErrorResponse(
            error = "Bad Request",
            message = exception.message ?: "Invalid request"
        )

        return ResponseEntity.badRequest().body(response)
    }
}