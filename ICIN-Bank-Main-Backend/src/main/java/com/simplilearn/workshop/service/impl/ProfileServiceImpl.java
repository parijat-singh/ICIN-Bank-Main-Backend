package com.simplilearn.workshop.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplilearn.workshop.dao.UserDisplayRepository;
import com.simplilearn.workshop.dao.UserRepository;
import com.simplilearn.workshop.details.UpdateDetails;
import com.simplilearn.workshop.model.User;
import com.simplilearn.workshop.model.UserDisplay;
import com.simplilearn.workshop.response.UpdateResponse;
import com.simplilearn.workshop.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService{

	@Autowired
	private UserRepository dao;
	
	@Autowired
	private UserDisplayRepository userDisplayDao;

	@Override
	public UpdateResponse updateUser(UpdateDetails user) {
		boolean flag=true;
		UpdateResponse response=new UpdateResponse();
		String message="Update successful"; 
		try {
			int counter = 0;
			User u=dao.findByUsername(user.getUsername());
			if(user.getAddress().length()!=0) {
				counter++;
				u.setAddress(user.getAddress());
			}
			if(user.getPassword().length()!=0 && user.getNewpassword().length()!=0) {
				counter++;
				String hashedPassword = DigestUtils.sha256Hex(user.getPassword());
				u.setPassword(hashedPassword);
			}
			if(user.getEmail().length()!=0) {
				counter++;
				u.setEmail(user.getEmail());
			}
			if(user.getPhone()!=0) {
				counter++;
				u.setPhone(user.getPhone());
			}
			System.out.println(counter);
			if(counter>0) {
				dao.save(u);
			}
			else {
				flag=false;
				message="Please enter some information to update";
			}
		}catch(Exception e){
			flag=false;
			response.setMessage("Update unsuccesful");
		}
		response.setMessage(message);
		response.setFlag(flag);
		return response;
	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
				return dao.findByUsername(username);

	}

	@Override
	public UserDisplay userDisplay(String username) {
		UserDisplay user=userDisplayDao.getCurrentUser(username);
		return user;
	}
	}

