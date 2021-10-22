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
	private SaccountRepository savrdata;
	
	@Autowired
	private UserHistoryService uhsdata;
	
	@Autowired
	private TransferHistoryService thsdata;
	
	@Autowired
	private AccountRepository ardata;
	
	@Autowired
	private UserRepository urdata;
	
	private static String acctPrefix = "5000000000";

	@Override
	public Saccount getAccount(String username) {	
		return savrdata.findByUsername(username);
	}
	
	@Override
	public Saccount getAccountDetails(long account) {
		// TODO Auto-generated method stub
		return savrdata.findByAccno(account);
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
		account.setUser(urdata.findByUsername(username));
		return savrdata.save(account);
	}

	@Override
	public DepositResponse deposit(long acc, int amount) {
		DepositResponse response=new DepositResponse();
		boolean flag=true;
		try {
			Saccount account=savrdata.findByAccno(acc);
			account.setBalance(account.getBalance()+amount);
			uhsdata.addAction(acc, amount, account.getBalance(), "deposit");
			savrdata.save(account);
			response.setResponseMessage("Success code 160: $"+amount+" successfully deposited. New account balance is $"+account.getBalance());
			response.setDepositStatus(flag);
		} catch (Exception e) {
			flag=false;
			response.setResponseMessage("Error code 160: Account number is incorrect");
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
			Saccount account=savrdata.findByAccno(acc);
			User user=urdata.findByUsername(account.getUsername());
			if(user.getFeatureStatus()==2 || user.getFeatureStatus()==3)
			{
			
			if(account.getBalance()>=amount) 
				{
				account.setBalance(account.getBalance()-amount);
				uhsdata.addAction(acc, amount, account.getBalance(), "withdraw");
				savrdata.save(account);
				response.setResponseMessage("Success code 161: $"+amount+" successfully withdrawn. New Account balance is $"+account.getBalance());
				response.setWithdrawStatus(flag);
				}
			else 
				{
				flag=false;
				response.setResponseMessage("Error code 162: NSF Error: Low balance in the account");
				response.setWithdrawStatus(flag);
				}
			}
			else {
				flag=false;
				response.setResponseMessage("Error code 162: Insufficient Privilege. Please contact Bank Admin.");
				response.setWithdrawStatus(flag);
			}
		} catch (Exception e) {
			flag=false;
			response.setResponseMessage("Error code 163: Account number is incorrect");
			response.setWithdrawStatus(flag);
		}
		response.setAccount(acc);
		return response;
	}

	@Override
	public TransferResponse transfer(long saccount, long raccount, int amount) {
		TransferResponse response=new TransferResponse();
		boolean flag=true;
		System.out.println("saccount" + saccount + "raccount" + raccount);
		try {
			Saccount senderAccount=savrdata.findByAccno(saccount);
			System.out.println("senderAccount" + senderAccount);
			if(isprimary(raccount)) {
				Account receiverAccount=ardata.findByAccno(raccount);
				if(senderAccount.getAccno()!=receiverAccount.getAccno()) 
				{
				if(senderAccount.getBalance()>=amount) {
					User user=urdata.findByUsername(senderAccount.getUsername());
					
					if(user.getFeatureStatus()==3) 
					{
						senderAccount.setBalance(senderAccount.getBalance()-amount);
						receiverAccount.setBalance(receiverAccount.getBalance()+amount);
						thsdata.addAction(saccount, raccount, amount);
						savrdata.save(senderAccount);
						ardata.save(receiverAccount);
						response.setResponseMessage("Success code 164: $"+amount+" successfully transferred to account "+receiverAccount.getAccno());
						response.setTransferStatus(flag);
					}
					else {
						flag=false;
						response.setResponseMessage("Error code 164: Insufficient Privilege. Please contact Bank Admin");
						response.setTransferStatus(flag);
					}
				}
				else 
					{
					flag=false;
					response.setResponseMessage("Error code 165: NSF Error: Balance too low to complete transaction");
					response.setTransferStatus(flag);
					}
			}
			else {
				flag=false;
				response.setResponseMessage("Error code 166: Source and traget Account numbers should be different");
				response.setTransferStatus(flag);
			}
			}
			else
			{
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
								savrdata.save(senderAccount);
								savrdata.save(receiverAccount);
								response.setResponseMessage("Success code 165: $"+amount+" successfully transferred to account "+receiverAccount.getAccno());
								response.setTransferStatus(flag);
							}
						else {
								flag=false;
								response.setResponseMessage("Error code 167: Insufficient privilege. Please contact Bank Admin");
								response.setTransferStatus(flag);
							}
						}
					
					else {
							flag=false;
							response.setResponseMessage("Error code 168: NSF Error: Account abalnce too low to complete transaction");
							response.setTransferStatus(flag);
						}
				}
				
				else {
						flag=false;
						response.setResponseMessage("Error code 169: Source and Traget Account cannot be same");
						response.setTransferStatus(flag);
				}
			}
		} catch (Exception e) {
			flag=false;
			response.setResponseMessage("Error code 1691: Account number is incorrect");
			response.setTransferStatus(flag);
		}
		response.setSaccount(saccount);
		return response;
	}
	
	public static boolean isprimary(long account) {
		String s = Long.toString(account).substring(0, 10);
		String check="1000000000";
		//System.out.println("From SaccountImpl: account=" + s + " check=" + check );
		if(s.equals(check)) {
			return true;
		}
		else 
		{
			return false;
		}
		
	}

}
