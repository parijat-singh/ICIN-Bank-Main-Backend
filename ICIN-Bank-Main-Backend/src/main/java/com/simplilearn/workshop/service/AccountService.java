package com.simplilearn.workshop.service;

import com.simplilearn.workshop.model.Account;
import com.simplilearn.workshop.resp.TransferResponse;

public interface AccountService {

	public Account newAccount(String username,int userId);
	public Account getAccount(String username);
	public TransferResponse transfer(long saccount,long raccount,int amount);
	public Account getAccountDetails(long account);
	public Account updateAccount(Account account);
}
