package com.simplilearn.workshop.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplilearn.workshop.model.User;
import com.simplilearn.workshop.response.RegisterResponse;
import com.simplilearn.workshop.service.RegistrationService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RegistrationController {
	
	@Autowired
	private RegistrationService service;

	@PostMapping("/register")
	public RegisterResponse createUser(@RequestBody User user) {

		return service.createAccount(user);
	}

}