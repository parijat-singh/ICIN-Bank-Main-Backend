package com.simplilearn.workshop.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplilearn.workshop.details.LoginDetails;
import com.simplilearn.workshop.model.User;
import com.simplilearn.workshop.repository.UserRepository;
import com.simplilearn.workshop.resp.LoginResponse;
import com.simplilearn.workshop.service.LoginService;

import org.apache.commons.codec.digest.DigestUtils;

@Service
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private UserRepository urdata;
	
	@Override
	public LoginResponse customerLogin(LoginDetails login){
		LoginResponse response = new LoginResponse();
		boolean flag = true;
		String message = "Success code 130: Login succesfull";
		User user = null;
		String hashedPassword = DigestUtils.sha256Hex(login.getPassword());
		try {
			user=urdata.findByUsername(login.getUsername());
			if(user.getStatus()) {
				flag = false;
				message = "Error code 130: Dear "+user.getFname()+" your account has been disabled. Please contact Bank Admin";
			}
			if(!user.getAuthorizationStatus()) {
				flag = false;
				message = "Error code 131: Dear "+user.getFname()+" Account pending Activation from the Admin";
			}
			if(!hashedPassword.equals(user.getPassword())) {
				flag = false;
				message = "Error code 132: Username or password is incorrect";
			}
		} 
		catch (Exception e) {
			flag = false;
			message = "Error code 133: Username or password is incorrect";
		}
		
		response.setLoginStatus(flag);
		response.setResponseMessage(message);
		try {
			response.setUsername(user.getUsername());
		} catch (Exception e) {
			response.setUsername(login.getUsername());
		}
		return response;
	}

}