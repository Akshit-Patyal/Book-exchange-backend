package com.book.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.book.exchange.entity.User;
import com.book.exchange.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public CustomUserDetails loadUserByUsername(String username) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		if (user.getIsactive() == 0) {
			throw new LockedException("User account is locked");
		}
		return CustomUserDetails.build(user);
	}

}
