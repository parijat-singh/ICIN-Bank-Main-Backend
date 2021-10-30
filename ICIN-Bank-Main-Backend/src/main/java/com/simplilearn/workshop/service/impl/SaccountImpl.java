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
