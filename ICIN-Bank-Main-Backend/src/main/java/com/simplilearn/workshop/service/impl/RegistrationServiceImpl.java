package com.simplilearn.workshop.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplilearn.workshop.model.User;
import com.simplilearn.workshop.repository.UserRepository;
import com.simplilearn.workshop.resp.RegisterResponse;
import com.simplilearn.workshop.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService{
	
	@Autowired
	private UserRepository urdata;
	
	@Override
	public RegisterResponse createAccount(User user){
		
		RegisterResponse response = new RegisterResponse();
		boolean flag = true;
		String message = "Success code 150: Registration Succesful";
		
		if(EmailAlreadyExists(user.getEmail())) {
			message = "Error code 150: Email already Exists";
			flag = false;
		}
		
		if(PhoneAlreadyExists(user.getPhone())) {
			message = "Error code 151: Phone number already Exists";
			flag = false;
		}
		
		if(usernameAlreadyExists(user.getUsername())) {
			message = "Error code 152: Username already Exists";
			flag = false;
		}
		
		if(flag) {
			String hashedPassword = DigestUtils.sha256Hex(user.getPassword());
			user.setPassword(hashedPassword);
			urdata.save(user);

		}
		response.setRegistrationStatus(flag);
		response.setResponseMessage(message);
		response.setUsername(user.getUsername());
		return response;
		
	}
	
	@Override
	public boolean usernameAlreadyExists(String username) {
		try {
			User u=urdata.findByUsername(username);
			System.out.println(u.toString());
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public boolean EmailAlreadyExists(String email) {
		try {
			User u=urdata.findByEmail(email);
			System.out.println(u.toString());
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	
	@Override
	public boolean PhoneAlreadyExists(long l) {
		try {
			User u=urdata.findByPhone(l);
			System.out.println(u.toString());
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	

}