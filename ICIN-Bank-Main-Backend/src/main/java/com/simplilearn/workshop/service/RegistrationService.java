package com.simplilearn.workshop.service;
import com.simplilearn.workshop.model.User;
import com.simplilearn.workshop.response.RegisterResponse;

public interface RegistrationService {

	public RegisterResponse createAccount(User details); 
	public boolean usernameAlreadyExists(String username);
	public boolean EmailAlreadyExists(String email);
	public boolean PhoneAlreadyExists(long l);
}
