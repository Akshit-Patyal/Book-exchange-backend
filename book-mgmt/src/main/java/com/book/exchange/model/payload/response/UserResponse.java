package com.book.exchange.model.payload.response;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(value = { "password" })
@JsonInclude(value = Include.NON_NULL)
public class UserResponse {
	private Long id;
	private Long attempts;
	private String email;
	private Long isactive;
	private String password;
	private Set<String> roles;
	private String username;
    
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String currentPassword;
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String newPassword;
	private String phone;
}