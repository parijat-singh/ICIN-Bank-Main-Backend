package com.simplilearn.workshop.service;

import com.simplilearn.workshop.details.LoginDetails;
import com.simplilearn.workshop.resp.LoginResponse;

public interface LoginService {

	public LoginResponse customerLogin(LoginDetails details);
}
