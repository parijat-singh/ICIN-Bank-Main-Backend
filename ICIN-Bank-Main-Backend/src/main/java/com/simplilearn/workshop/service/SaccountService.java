package com.simplilearn.workshop.service;

import com.simplilearn.workshop.model.Saccount;
import com.simplilearn.workshop.resp.TransferResponse;

public interface SaccountService {
	public Saccount getAccountDetails(long account);
	public Saccount getAccount(String username);
	public Saccount newAccount(String username,int userId);
	public TransferResponse transfer(long saccount,long raccount,int amount);
}

