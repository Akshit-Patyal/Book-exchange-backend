package com.book.exchange.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.book.exchange.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = -8697603848255048660L;
	
	private Long user_id;
	private String username;
	private String email;
	private Long isactive;
	private Long attempts;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private String phone;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isactive.intValue() == 1;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isactive.intValue() == 1;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !(attempts.intValue() >= 5);
	}

	@Override
	public boolean isEnabled() {
		return isactive.intValue() == 1;
	}
	
	public static CustomUserDetails build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());
		
		return new CustomUserDetails(user.getId(), 
				user.getUsername(), 
				user.getEmail(),				
				user.getIsactive(), 
				user.getAttempts(), 
				user.getPassword(), 
				authorities,
				user.getPhone());
	}
}
