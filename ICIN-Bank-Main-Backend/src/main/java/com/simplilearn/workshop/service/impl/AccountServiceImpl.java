package com.simplilearn.workshop.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplilearn.workshop.model.Account;
import com.simplilearn.workshop.model.Saccount;
import com.simplilearn.workshop.model.User;
import com.simplilearn.workshop.repository.AccountRepository;
import com.simplilearn.workshop.repository.SaccountRepository;
import com.simplilearn.workshop.repository.UserRepository;
import com.simplilearn.workshop.response.DepositResponse;
import com.simplilearn.workshop.response.TransferResponse;
import com.simplilearn.workshop.response.WithdrawResponse;
import com.simplilearn.workshop.service.AccountService;
import com.simplilearn.workshop.service.TransferHistoryService;
import com.simplilearn.workshop.service.UserHistoryService;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountRepository dao;
	
	@Autowired
	private UserHistoryService service;
	
	@Autowired
	private TransferHistoryService tservice;
	
	@Autowired
	private UserRepository udao;
	
	@Autowired
	private SaccountRepository sdao;
	
	private static String acctPrefix = "1000000000";

	public long generate_saving(int userId) {
		String accNo = acctPrefix+String.valueOf(userId);
		return Long.parseLong(accNo);
	}
	
	public static boolean isprimary(long account) {
		String s = Long.toString(account).substring(0, 10);
		String check="1000000000";
		System.out.println("From AccountServiceImpl: account=" + s + " check=" + check );
		if(s.equals(check)) {
			return true;
		}
		else 
		{
			return false;
		}
		
	}

	@Override
	public Account newAccount(String username,int userId) {
		Account account=new Account();
		account.setUsername(username);
		account.setAccno(generate_saving(userId));
		account.setUser(udao.findByUsername(username));
		return dao.save(account);

	}

	@Override
	public Account getAccount(String username) {
		// TODO Auto-generated method stub
		return dao.findByUsername(username);
	}

	@Override
	public DepositResponse deposit(long acc, int amount) {
		DepositResponse response=new DepositResponse();
		
		boolean flag=true;
		try {
			Account account=dao.findByAccno(acc);
			account.setBalance(account.getBalance()+amount);
			service.addAction(acc, amount, account.getBalance(), "credit");
			dao.save(account);
			response.setResponseMessage("Success Code 100 - $"+amount+" successfully deposited. New balance is $"+account.getBalance());
			response.setDepositStatus(flag);
		} 
		catch (Exception e) {
			flag=false;
			response.setResponseMessage("Error code 100: Checking Account number is incorrect");
			response.setDepositStatus(flag);
		}
		response.setAccount(acc);
		return response; 
	}

	@Override
	public WithdrawResponse withdraw(long acc, int amount) {
		WithdrawResponse response=new WithdrawResponse();
		boolean flag=true;
		try {
			Account account=dao.findByAccno(acc);
			User user=udao.findByUsername(account.getUsername());
			if(user.getFeatureStatus()==2 || user.getFeatureStatus()==3)
			{
			if(account.getBalance()>=amount) 
				{
					account.setBalance(account.getBalance()-amount);
					service.addAction(acc, amount, account.getBalance(), "debit");
					dao.save(account);
					response.setResponseMessage("Success code 101: $"+amount+" successfully withdrawn. New account balance: $"+account.getBalance());
					response.setWithdrawStatus(flag);
				}
			else 
				{
					flag=false;
					response.setResponseMessage("Error Code 101: NSF Error: Account balance lower than needed for transaction!");
					response.setWithdrawStatus(flag);
				}
			}
			else {
				flag=false;
				response.setResponseMessage("Error Code 102:Isufficient Account Provilege. Please contact Bank Admin.");
				response.setWithdrawStatus(flag);
			}
			
		} 
		
		catch (Exception e) {
			flag=false;
			response.setResponseMessage("Error Code 103: Account number is incorrect");
			response.setWithdrawStatus(flag);
		}
		
		response.setAccount(acc);
		return response;
	}

	@Override
	public TransferResponse transfer(long saccount, long raccount, int amount) {
		TransferResponse response=new TransferResponse();
		boolean flag=true;
		
		try {
			Account senderAccount=dao.findByAccno(saccount);
			if(isprimary(raccount))
			{
				Account receiverAccount=dao.findByAccno(raccount);
				if(senderAccount.getAccno()!=receiverAccount.getAccno()) 
				{
					if(senderAccount.getBalance()>amount) {
						User user=udao.findByUsername(senderAccount.getUsername());
						
						if(user.getFeatureStatus()==3) 
						{
						senderAccount.setBalance(senderAccount.getBalance()-amount);
						receiverAccount.setBalance(receiverAccount.getBalance()+amount);
						tservice.addAction(saccount, raccount, amount);
						dao.save(senderAccount);
						dao.save(receiverAccount);
						response.setResponseMessage("Success code 103: $"+amount+" successfully transferred to account "+receiverAccount.getAccno());
						response.setTransferStatus(flag);
						}
						else {
							flag=false;
							response.setResponseMessage("Error Code 104: Insufficient Privilege. Please contact Bank Admin!");
							response.setTransferStatus(flag);
						}
					}
					else {
						flag=false;
						response.setResponseMessage("Error Code 105: NSF Error: Balance lower then needed to complete transaction!");
						response.setTransferStatus(flag);
						}
				}
				else {
					flag=false;
					response.setResponseMessage("Error Code 106: Cannot send money to the same account!");
					response.setTransferStatus(flag);
				}
			}
			else {
				Saccount receiverAccount=sdao.findByAccno(raccount);
				if(senderAccount.getAccno()!=receiverAccount.getAccno()) 
				{
					if(senderAccount.getBalance()>amount) {
						
						User user=udao.findByUsername(senderAccount.getUsername());
						
						if(user.getFeatureStatus()==3) 
							{
						senderAccount.setBalance(senderAccount.getBalance()-amount);
						receiverAccount.setBalance(receiverAccount.getBalance()+amount);
						tservice.addAction(saccount, raccount, amount);
						dao.save(senderAccount);
						sdao.save(receiverAccount);
						response.setResponseMessage("Success code 105: $"+amount+" successfully transferred to account "+receiverAccount.getAccno());
						response.setTransferStatus(flag);
							}
						else {
							flag=false;
							response.setResponseMessage("Error Code 107: Insufficient Privilege. Please contact Bank Admin!");
							response.setTransferStatus(flag);
						}
						}
					else {
						flag=false;
						response.setResponseMessage("Error Code 108: NSF Error: Balance lower then needed to complete transaction!");
						response.setTransferStatus(flag);
						}
				}
				else {
					flag=false;
					response.setResponseMessage("Error Code 109: Cannot Transfer money to same account");
					response.setTransferStatus(flag);
				}
			}
		} 
		
		catch (Exception e) {
			flag=false;
			response.setResponseMessage("Error Code 110: Account number is incorrect");
			response.setTransferStatus(flag);
		}
		response.setSaccount(saccount);
		return response;
	}

	@Override
	public Account getAccountDetails(long account) {
		// TODO Auto-generated method stub
		return dao.findByAccno(account);
	}

	@Override
	public Account updateAccount(Account account) {
		// TODO Auto-generated method stub
		return dao.save(account);
	}

}
