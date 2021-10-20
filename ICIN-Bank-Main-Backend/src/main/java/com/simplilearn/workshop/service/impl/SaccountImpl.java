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
import com.simplilearn.workshop.service.SaccountService;
import com.simplilearn.workshop.service.TransferHistoryService;
import com.simplilearn.workshop.service.UserHistoryService;
@Service
public class SaccountImpl implements SaccountService{
	
	@Autowired
	private SaccountRepository dao;
	
	@Autowired
	private UserHistoryService service;
	
	@Autowired
	private TransferHistoryService tservice;
	
	@Autowired
	private AccountRepository adao;
	
	@Autowired
	private UserRepository udao;
	
	private static String acctPrefix = "5000000000";

	@Override
	public Saccount getAccount(String username) {	
		return dao.findByUsername(username);
	}
	
	@Override
	public Saccount getAccountDetails(long account) {
		// TODO Auto-generated method stub
		return dao.findByAccno(account);
	}
	
	public long generate_saving(int userId) {
		String accNo = acctPrefix+String.valueOf(userId);
		return Long.parseLong(accNo);
	}

	@Override
	public Saccount newAccount(String username, int userId) {
		Saccount account =new Saccount();
		account.setUsername(username);
		account.setAccno(generate_saving(userId));
		account.setUser(udao.findByUsername(username));
		return dao.save(account);
	}

	@Override
	public DepositResponse deposit(long acc, int amount) {
		DepositResponse response=new DepositResponse();
		boolean flag=true;
		try {
			Saccount account=dao.findByAccno(acc);
			account.setBalance(account.getBalance()+amount);
			service.addAction(acc, amount, account.getBalance(), "deposit");
			dao.save(account);
			response.setResponseMessage("$"+amount+" successfully deposited into your account balance is now $"+account.getBalance());
			response.setDepositStatus(flag);
		} catch (Exception e) {
			flag=false;
			response.setResponseMessage("Account number is incorrect");
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
			Saccount account=dao.findByAccno(acc);
			User user=udao.findByUsername(account.getUsername());
			if(user.getFeatureStatus()==2 || user.getFeatureStatus()==3)
			{
			
			if(account.getBalance()>=amount) 
				{
				account.setBalance(account.getBalance()-amount);
				service.addAction(acc, amount, account.getBalance(), "withdraw");
				dao.save(account);
				response.setResponseMessage("$"+amount+" successfully withdrawn your account balance is now $"+account.getBalance());
				response.setWithdrawStatus(flag);
				}
			else 
				{
				flag=false;
				response.setResponseMessage("Insufficient funds to complete the transaction");
				response.setWithdrawStatus(flag);
				}
			}
			else {
				flag=false;
				response.setResponseMessage("This function is not available for your account");
				response.setWithdrawStatus(flag);
			}
		} catch (Exception e) {
			flag=false;
			response.setResponseMessage("Account number is incorrect");
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
			Saccount senderAccount=dao.findByAccno(saccount);
			if(isprimary(raccount)) {
				Account receiverAccount=adao.findByAccno(raccount);
				if(senderAccount.getAccno()!=receiverAccount.getAccno()) 
				{
				if(senderAccount.getBalance()>=amount) {
					User user=udao.findByUsername(senderAccount.getUsername());
					
					if(user.getFeatureStatus()==3) 
					{
						senderAccount.setBalance(senderAccount.getBalance()-amount);
						receiverAccount.setBalance(receiverAccount.getBalance()+amount);
						tservice.addAction(saccount, raccount, amount);
						dao.save(senderAccount);
						adao.save(receiverAccount);
						response.setResponseMessage("$"+amount+" successfully transferred to account "+receiverAccount.getAccno());
						response.setTransferStatus(flag);
					}
					else {
						flag=false;
						response.setResponseMessage("This feature is not available for your account");
						response.setTransferStatus(flag);
					}
				}
				else 
					{
					flag=false;
					response.setResponseMessage("Insufficient funds to complete the transfer");
					response.setTransferStatus(flag);
					}
			}
			else {
				flag=false;
				response.setResponseMessage("sender and recieiver accounts are same");
				response.setTransferStatus(flag);
			}
			}
			else
			{
				Saccount receiverAccount=dao.findByAccno(raccount);
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
								response.setResponseMessage("$"+amount+" successfully transferred to account "+receiverAccount.getAccno());
								response.setTransferStatus(flag);
							}
						else {
								flag=false;
								response.setResponseMessage("This function isnt available for the account");
								response.setTransferStatus(flag);
							}
						}
					
					else {
							flag=false;
							response.setResponseMessage("Insufficient funds to complete the transfer");
							response.setTransferStatus(flag);
						}
				}
				
				else {
						flag=false;
						response.setResponseMessage("sender and recieiver accounts are same");
						response.setTransferStatus(flag);
				}
			}
		} catch (Exception e) {
			flag=false;
			response.setResponseMessage("Account number is incorrect");
			response.setTransferStatus(flag);
		}
		response.setSaccount(saccount);
		return response;
	}
	
	public static boolean isprimary(long account) {
		String s = Long.toString(account).substring(0, 10);
		String check="1000000000";
		System.out.println("From SaccountImpl: account=" + s + " check=" + check );
		if(s.equals(check)) {
			return true;
		}
		else 
		{
			return false;
		}
		
	}

}
