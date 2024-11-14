package com.book.exchange.model.payload.request;

import java.util.Set;

import com.book.exchange.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProfileUpdateRequest {


	@NotNull
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20")
	private String username;

	@Size(max = 50)
	@Email
	private String email;

	private Set<Role> roles;
	
	@Size(min = 6, max = 40, message = "Password must be between 6 and 40")
	private String currentPassword;

	@Size(min = 6, max = 40, message = "Password must be between 6 and 40")
	private String newPassword;
	
	@Size(max = 10)
	private String phone;
}
