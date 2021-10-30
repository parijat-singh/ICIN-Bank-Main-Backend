package com.simplilearn.workshop.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplilearn.workshop.model.Account;
import com.simplilearn.workshop.model.ChequebookRequest;
import com.simplilearn.workshop.model.Saccount;
import com.simplilearn.workshop.repository.AccountRepository;
import com.simplilearn.workshop.repository.ChequeBookRepository;
import com.simplilearn.workshop.repository.SaccountRepository;
import com.simplilearn.workshop.resp.ChequeResponse;
import com.simplilearn.workshop.service.ChequebookService;

@Service
public class ChequebookServiceimpl implements ChequebookService{

	@Autowired
	private ChequeBookRepository cbrdata;
	
	@Autowired 
	private AccountRepository ardata;
	
	@Autowired
	private SaccountRepository savrdata;
	
	@Override
	public ChequeResponse createrequest(ChequebookRequest chequebook) {
		ChequeResponse response=new ChequeResponse();
		long account = chequebook.getAccount();
		List<ChequebookRequest> prevRequests = cbrdata.findByAccount(account);
		if(!prevRequests.isEmpty()) {
			for(int i=0;i<prevRequests.size();i++) {
				if(prevRequests.get(i).isRequestStatus()==false) {
					response.setResponseMessage("Error code 120: Your previous chequebook request is still pending.");
					response.setStatus(false);
					response.setAccount(account);
					return response;
				}
			}
		}
		LocalDate today = LocalDate.now();
		if(isprimary(account)) {
			try {
				Account account1 =ardata.findByAccno(account);
				response.setAccount(account1.getAccno());
				response.setStatus(true);
				response.setResponseMessage("Success Code 120: Request submitted successfully");
				chequebook.setAccType("Primary");
				chequebook.setDate(today);
				chequebook.setRequestStatus(false);
				cbrdata.save(chequebook);
				}
				catch(Exception e) {
					response.setAccount(account);
					response.setStatus(false);
					response.setResponseMessage("Error code 121: Primary account number is incorrect");
					System.out.println(e);
				}
		}
		else {
			if(isSecondary(account)) {
				try {
					Saccount saccount=savrdata.findByAccno(account);
					response.setAccount(saccount.getAccno());
					response.setStatus(true);
					response.setResponseMessage("Success Code 121: Request submitted successfully");
					chequebook.setRequestStatus(false);
					chequebook.setAccType("Secondary");
					chequebook.setDate(today);
					cbrdata.save(chequebook);
					} 
				catch (Exception e) {
					response.setAccount(account);
					response.setStatus(false);
					response.setResponseMessage("Error code 122: Savings account number is incorrect");
					}
		}
		else
		{
			response.setAccount(account);
			response.setStatus(false);
			response.setResponseMessage("Error code 123: Savings account number is incorrect");
		}
		
		}
		return response;
	}

	@Override
	public List<ChequebookRequest> getRequests(long account) {
		return cbrdata.findByAccount(account);
	}

	public static boolean isprimary(long account) {
		String s = Long.toString(account).substring(0, 10);
		String check="1000000000";
		//System.out.println("From ChequebookServiceImpl: account=" + s + " check=" + check );
		if(s.equals(check)) {
			return true;
		}
		else 
		{
			return false;
		}
		
	}
	
	public static boolean isSecondary(long account) {
		String s = Long.toString(account).substring(0, 10);
		String check="5000000000";
		//System.out.println("From ChequebookServiceImpl: account=" + s + " check=" + check );
		if(s.equals(check)) {
			return true;
		}
		else 
		{
			return false;
		}
		
	}
}
