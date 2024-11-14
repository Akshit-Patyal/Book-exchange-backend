package com.book.exchange.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.book.exchange.entity.User;
import com.book.exchange.model.payload.request.SignInRequest;
import com.book.exchange.model.payload.response.TokenResponse;
import com.book.exchange.model.payload.response.UserResponse;
import com.book.exchange.repository.UserRepository;
import com.book.exchange.security.UserReturnDataContextHolder;
import com.book.exchange.utility.JWTUtility;

@Service
public class SignInService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private UserRepository userRepository;

	public TokenResponse signIn(final SignInRequest singInRequest) {

		UserReturnDataContextHolder.setLoginRequest(singInRequest);
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(singInRequest.getUsername(), singInRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtility.generateJwtToken(authentication);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Set<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toSet());

		User user = userRepository.findByUsername(singInRequest.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException(" User is not found."));
		Long attempts = user.getAttempts();

		if (attempts >= 1) {
			user.setAttempts(0L);
			userRepository.saveAndFlush(user);
		}

		UserResponse userResponse = UserResponse.builder().id(userDetails.getUser_id()).attempts(user.getAttempts())
				.username(userDetails.getUsername()).email(userDetails.getEmail()).phone(userDetails.getPhone())
				.isactive(userDetails.getIsactive()).roles(roles).build();

		return new TokenResponse(jwt, userResponse);
	}
}
