package com.simplilearn.workshop.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplilearn.workshop.dao.UserHistoryRepository;
import com.simplilearn.workshop.model.UserHistory;
import com.simplilearn.workshop.service.UserHistoryService;

@Service
public class UserHistoryServiceImpl implements UserHistoryService{

	@Autowired
	private UserHistoryRepository dao;
	
	@Override
	public UserHistory addAction(long account, int amount, int balance, String action) {
		LocalDate today = LocalDate.now();
		
		UserHistory row=new UserHistory();
		row.setAccount(account);
		row.setAction(action);
		row.setAmount(amount);
		row.setDate(today);
		return dao.save(row);
	}

	@Override
	public List<UserHistory> getHistory(long account) {
		// TODO Auto-generated method stub
				return dao.findByAccount(account);
	}

}
