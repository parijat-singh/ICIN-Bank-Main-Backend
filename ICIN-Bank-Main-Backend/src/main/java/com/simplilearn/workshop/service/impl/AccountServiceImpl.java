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
import com.simplilearn.workshop.resp.TransferResponse;
import com.simplilearn.workshop.service.AccountService;
import com.simplilearn.workshop.service.TransferHistoryService;
import com.simplilearn.workshop.service.UserHistoryService;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountRepository ardata;
	
	@Autowired
	private UserHistoryService uhsdata;
	
	@Autowired
	private TransferHistoryService thsdata;
	
	@Autowired
	private UserRepository urdata;
	
	@Autowired
	private SaccountRepository savrdata;
	
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
		account.setUser(urdata.findByUsername(username));
		return ardata.save(account);

	}

	@Override
	public Account getAccount(String username) {
		// TODO Auto-generated method stub
		return ardata.findByUsername(username);
	}

	@Override
	public TransferResponse transfer(long saccount, long raccount, int amount) {
		TransferResponse response=new TransferResponse();
		boolean flag=true;
		
		try {
			Account senderAccount=ardata.findByAccno(saccount);
			if(isprimary(raccount))
			{
				Account receiverAccount=ardata.findByAccno(raccount);
				if(senderAccount.getAccno()!=receiverAccount.getAccno()) 
				{
					if(senderAccount.getBalance()>amount) {
						User user=urdata.findByUsername(senderAccount.getUsername());
						
						if(user.getFeatureStatus()==3) 
						{
						senderAccount.setBalance(senderAccount.getBalance()-amount);
						receiverAccount.setBalance(receiverAccount.getBalance()+amount);
						thsdata.addAction(saccount, raccount, amount);
						ardata.save(senderAccount);
						ardata.save(receiverAccount);
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
				Saccount receiverAccount=savrdata.findByAccno(raccount);
				if(senderAccount.getAccno()!=receiverAccount.getAccno()) 
				{
					if(senderAccount.getBalance()>amount) {
						
						User user=urdata.findByUsername(senderAccount.getUsername());
						
						if(user.getFeatureStatus()==3) 
							{
						senderAccount.setBalance(senderAccount.getBalance()-amount);
						receiverAccount.setBalance(receiverAccount.getBalance()+amount);
						thsdata.addAction(saccount, raccount, amount);
						ardata.save(senderAccount);
						savrdata.save(receiverAccount);
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
		return ardata.findByAccno(account);
	}

	@Override
	public Account updateAccount(Account account) {
		// TODO Auto-generated method stub
		return ardata.save(account);
	}

}
