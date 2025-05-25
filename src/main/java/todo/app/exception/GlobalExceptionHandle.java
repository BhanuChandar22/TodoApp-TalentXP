package todo.app.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<ErrorMessage> notFound(UserNotFoundException userNotFoundException) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(userNotFoundException.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.name()));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorMessage> resourceExpired(ExpiredJwtException jwtException) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorMessage(jwtException.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.name()));
	}
}