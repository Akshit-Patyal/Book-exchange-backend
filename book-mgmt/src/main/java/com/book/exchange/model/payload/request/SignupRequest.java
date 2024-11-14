package com.book.exchange.model.payload.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {

	@NotBlank
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20")
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	private Set<String> roles;

	@NotBlank
	@Size(min = 6, max = 40, message = "Password must be between 6 and 40")
	private String password;

	@Size(max = 10)
	@NonNull
	private String phone;
}
