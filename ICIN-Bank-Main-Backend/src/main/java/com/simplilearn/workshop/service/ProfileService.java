package com.simplilearn.workshop.service;

import com.simplilearn.workshop.details.UpdateDetails;
import com.simplilearn.workshop.model.User;
import com.simplilearn.workshop.model.UserDisplay;
import com.simplilearn.workshop.resp.UpdateResponse;

public interface ProfileService {
	public UpdateResponse updateUser(UpdateDetails user);
	public User getUser(String username);
	public UserDisplay userDisplay(String username);

}
