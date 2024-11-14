package com.book.exchange.model.payload.request;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class SignInRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
