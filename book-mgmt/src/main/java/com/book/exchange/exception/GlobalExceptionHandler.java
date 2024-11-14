package com.book.exchange.exception;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.book.exchange.entity.User;
import com.book.exchange.repository.UserRepository;
import com.book.exchange.security.UserReturnDataContextHolder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private UserRepository userRepository;

	@ExceptionHandler({ AuthenticationException.class, BadCredentialsException.class })
	public ResponseEntity<ErrorMessage> securityLoginRelatedException(HttpServletRequest request,
			AuthenticationException ae) throws Exception {
		String userName = UserReturnDataContextHolder.getSignInRequest().getUsername();
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new RuntimeException(" User is not found."));
		Long attempts = user.getAttempts();

		if (user.getIsactive() == 1) {
			user.setAttempts(++attempts);
			userRepository.save(user);
		}

		if (attempts == 3) {
			user.setIsactive(0L);
			userRepository.save(user);
		}

		UserReturnDataContextHolder.clearContext();
		ErrorMessage message = ErrorMessage.builder().code(HttpStatus.UNAUTHORIZED.value())
				.path(request.getServletPath()).message(ae.getMessage())
				.status("Only " + (3 - attempts) + " attempts left.").build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> argumentNotValid(HttpServletRequest request,
			MethodArgumentNotValidException anve) {

		String completeErrorDetails = anve.getFieldErrors().stream()
				.map(f -> f.getCode() + " : " + f.getField() + " : " + f.getDefaultMessage())
				.collect(Collectors.joining("  "));

		ErrorMessage errorBody = ErrorMessage.builder().code(anve.getStatusCode().value())
				.status(HttpStatus.BAD_REQUEST.name()).message(completeErrorDetails).timeStamp(LocalDateTime.now())
				.path(request.getServletPath()).build();

		return ResponseEntity.badRequest().body(errorBody);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorMessage> notReadableExcpetion(HttpServletRequest request,
			HttpMessageNotReadableException nre) {

		ErrorMessage errorBody = ErrorMessage.builder().code(HttpStatus.BAD_REQUEST.value())
				.path(request.getServletPath()).status(HttpStatus.BAD_REQUEST.name()).message(nre.getMessage())
				.timeStamp(LocalDateTime.now()).build();

		return ResponseEntity.badRequest().body(errorBody);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorMessage> notReadableExcpetion(HttpServletRequest request,
			DataIntegrityViolationException dive) {

		ErrorMessage errorBody = ErrorMessage.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.name()).message("Resource already available.")
				.timeStamp(LocalDateTime.now()).path(request.getServletPath()).build();

		return ResponseEntity.internalServerError().body(errorBody);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorMessage> invalidTokenException(HttpServletRequest request, InvalidTokenException it) {

		ErrorMessage errorBody = ErrorMessage.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.name()).message(it.getMessage()).timeStamp(LocalDateTime.now())
				.path(request.getServletPath()).build();

		return ResponseEntity.internalServerError().body(errorBody);
	}

	@ExceptionHandler(ResourceAlreadyAvailable.class)
	public ResponseEntity<ErrorMessage> resourceAlreadyAvailable(HttpServletRequest request,
			ResourceAlreadyAvailable ra) {

		ErrorMessage errorBody = ErrorMessage.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.name()).message(ra.getMessage()).timeStamp(LocalDateTime.now())
				.path(request.getServletPath()).build();

		return ResponseEntity.internalServerError().body(errorBody);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> entityNotFoundException(HttpServletRequest request,
			EntityNotFoundException en) {

		ErrorMessage errorBody = ErrorMessage.builder().code(HttpStatus.NOT_FOUND.value())
				.status(HttpStatus.NOT_FOUND.name()).message(en.getMessage()).timeStamp(LocalDateTime.now())
				.path(request.getServletPath()).build();

		return ResponseEntity.internalServerError().body(errorBody);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorMessage> handleHttpRequestMethodNotSupported(HttpServletRequest request,
			HttpRequestMethodNotSupportedException ex) {
		String message = "HTTP method not supported: " + ex.getMethod() + ". Supported methods are: "
				+ ex.getSupportedHttpMethods();
		ErrorMessage errorBody = ErrorMessage.builder().code(HttpStatus.METHOD_NOT_ALLOWED.value()).message(message)
				.status(HttpStatus.METHOD_NOT_ALLOWED.name()).path(request.getServletPath())
				.timeStamp(LocalDateTime.now()).build();
		return new ResponseEntity<ErrorMessage>(errorBody, HttpStatus.METHOD_NOT_ALLOWED);
	}
}