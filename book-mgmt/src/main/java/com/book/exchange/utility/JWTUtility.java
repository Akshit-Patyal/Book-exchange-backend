package com.book.exchange.utility;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.book.exchange.exception.InvalidTokenException;
import com.book.exchange.service.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JWTUtility {

	Logger logger = LoggerFactory.getLogger(JWTUtility.class);

	@Value("${book.jwtSecret}")
	private String jwtSecret;

	@Value("${book.jwtExpiration}")
	private int jwtExpiration;

	public String generateJwtToken(Authentication authentication) {

		CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

		return Jwts.builder()
				.subject(userPrincipal.getUsername())
				.claim("username", userPrincipal.getUsername())
				.claim("email", userPrincipal.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.issuer("https://www.prashanthsagari.online")
				.signWith(key())
				.compact();
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String extractUsernameFromToken(String token) {
		if (!validateJwtToken(token)) {
			return null;
		}
		return getClaims(token, Claims::getSubject);
	}

	public <T> T getClaims(String token, Function<Claims, T> resolver) {
		return resolver.apply(Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload());
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Date expiration = getClaims(authToken, Claims::getExpiration);
			return !expiration.before(new Date());  // Returns true if invalid
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
			throw new InvalidTokenException(e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
			throw new InvalidTokenException(e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
			throw new InvalidTokenException(e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
			throw new InvalidTokenException(e.getMessage());
		} catch(SignatureException se) {
			logger.error("Modified JWT token: {}", se.getMessage());
			throw new InvalidTokenException(se.getMessage());
		}
	}
}
