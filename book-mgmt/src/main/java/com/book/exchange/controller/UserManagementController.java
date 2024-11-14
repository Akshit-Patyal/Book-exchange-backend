package com.book.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.exchange.model.payload.request.ProfileUpdateRequest;
import com.book.exchange.service.ProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/profile", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "UserManagementController", description = "User management service")
public class UserManagementController {

	@Autowired
	private ProfileService profileService;
	
	@PutMapping("/update")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> updateDetails(@RequestBody @Validated ProfileUpdateRequest profileUpdateRequest) {
		return ResponseEntity.ok(profileService.update(profileUpdateRequest));
	}

}
