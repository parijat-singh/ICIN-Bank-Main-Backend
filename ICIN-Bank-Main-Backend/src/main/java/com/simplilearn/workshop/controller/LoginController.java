package com.simplilearn.workshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplilearn.workshop.details.LoginDetails;
import com.simplilearn.workshop.response.LoginResponse;
import com.simplilearn.workshop.service.impl.LoginServiceImpl;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {
	
	@Autowired
	LoginServiceImpl service;
	
	@PostMapping("/login")
	public LoginResponse userLogin(@RequestBody LoginDetails details) {
		
		return service.customerLogin(details);
		
	}

}