package com.simplilearn.workshop.service;

import com.simplilearn.workshop.model.Saccount;
import com.simplilearn.workshop.response.DepositResponse;
import com.simplilearn.workshop.response.TransferResponse;
import com.simplilearn.workshop.response.WithdrawResponse;

public interface SaccountService {
	public Saccount getAccountDetails(long account);
	public Saccount getAccount(String username);
	public Saccount newAccount(String username,int userId);
	public DepositResponse deposit(long acc,int amount);
	public WithdrawResponse withdraw(long acc,int amount);
	public TransferResponse transfer(long saccount,long raccount,int amount);
}

