package com.book.exchange.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.book.exchange.entity.Role;
import com.book.exchange.entity.User;
import com.book.exchange.enums.ERole;
import com.book.exchange.exception.ResourceAlreadyAvailable;
import com.book.exchange.model.payload.request.SignupRequest;
import com.book.exchange.model.payload.response.UserResponse;
import com.book.exchange.repository.RoleRepository;
import com.book.exchange.repository.UserRepository;

@Service
public class SignUpService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserResponse registerUser(final SignupRequest signUpRequest) {
		
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new ResourceAlreadyAvailable("Error: Username is already taken!");
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new ResourceAlreadyAvailable("Error: Email is already in use!");
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				passwordEncoder.encode(signUpRequest.getPassword()), 1l, 0l, signUpRequest.getPhone());

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		return UserResponse.builder()
				.username(user.getUsername())
				.email(user.getEmail())
				.phone(user.getPhone())
				.roles(user.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toSet()))
				.build();
	}
}
