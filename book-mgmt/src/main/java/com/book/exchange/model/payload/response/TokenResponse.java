package com.book.exchange.model.payload.response;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class TokenResponse {

	public TokenResponse(String accessToken, UserResponse userResponse) {
		this.accessToken = accessToken;
		this.userResponse = userResponse;
	}

	private String accessToken;
//	private final String type = "Bearer";
	private UserResponse userResponse;
}