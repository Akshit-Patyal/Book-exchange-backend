package com.book.exchange.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.book.exchange.model.payload.request.SignInRequest;

public class UserReturnDataContextHolder extends SecurityContextHolder {

	public static SignInRequest signInRequest = new SignInRequest();

	public static SignInRequest getSignInRequest() {
		return signInRequest;
	}

	public static void setLoginRequest(SignInRequest signInRequest) {
		UserReturnDataContextHolder.signInRequest = signInRequest;
	}

}