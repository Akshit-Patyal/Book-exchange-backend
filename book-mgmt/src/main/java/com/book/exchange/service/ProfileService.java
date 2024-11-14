package com.book.exchange.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.book.exchange.entity.User;
import com.book.exchange.model.payload.request.ProfileUpdateRequest;
import com.book.exchange.model.payload.response.UserResponse;
import com.book.exchange.repository.UserRepository;

@Service
public class ProfileService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserResponse update(ProfileUpdateRequest profileUpdateRequest) {
		User user = userRepository.findByUsername(profileUpdateRequest.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException(" User is not found."));
		updateProfile(profileUpdateRequest, user);
		userRepository.saveAndFlush(user);
		return UserResponse.builder()
				.username(user.getUsername()).email(user.getEmail()).id(user.getId()).phone(user.getPhone())
				.roles(user.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toSet()))
				.build();
	}

	private void updateProfile(ProfileUpdateRequest profileUpdateRequest, User user) {

		if (StringUtils.hasText(profileUpdateRequest.getCurrentPassword())
				&& StringUtils.hasText(profileUpdateRequest.getNewPassword())
				&& passwordEncoder.matches(profileUpdateRequest.getCurrentPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(profileUpdateRequest.getNewPassword()));
		}

		if (StringUtils.hasText(profileUpdateRequest.getEmail())) {
			user.setEmail(profileUpdateRequest.getEmail());
		}

		if (StringUtils.hasText(profileUpdateRequest.getPhone())) {
			user.setPhone(profileUpdateRequest.getPhone());
		}

		if (!ObjectUtils.isEmpty(profileUpdateRequest.getRoles())) {
			user.setRoles(profileUpdateRequest.getRoles());
		}
	}
}
