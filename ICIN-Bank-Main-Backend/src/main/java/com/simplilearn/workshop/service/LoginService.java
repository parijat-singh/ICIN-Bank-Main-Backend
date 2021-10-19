package com.simplilearn.workshop.service;

import com.simplilearn.workshop.details.LoginDetails;
import com.simplilearn.workshop.response.LoginResponse;

public interface LoginService {

	public LoginResponse customerLogin(LoginDetails details);
}
