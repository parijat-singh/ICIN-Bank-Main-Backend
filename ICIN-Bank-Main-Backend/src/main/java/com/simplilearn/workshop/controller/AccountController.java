package com.simplilearn.workshop.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplilearn.workshop.details.TransactionDetails;
import com.simplilearn.workshop.details.TransferDetails;
import com.simplilearn.workshop.model.Account;
import com.simplilearn.workshop.model.Saccount;
import com.simplilearn.workshop.model.Transfer;
import com.simplilearn.workshop.model.UserHistory;
import com.simplilearn.workshop.repository.AccountRepository;
import com.simplilearn.workshop.repository.SaccountRepository;
import com.simplilearn.workshop.response.DepositResponse;
import com.simplilearn.workshop.response.TransferResponse;
import com.simplilearn.workshop.response.WithdrawResponse;
import com.simplilearn.workshop.service.AccountService;
import com.simplilearn.workshop.service.SaccountService;
import com.simplilearn.workshop.service.TransferHistoryService;
import com.simplilearn.workshop.service.UserHistoryService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

	@Autowired
	private AccountService service;

	@Autowired
	private SaccountService savservice;

	@Autowired
	private UserHistoryService histservice;

	@Autowired
	private TransferHistoryService tservice;

	@Autowired
	private AccountRepository adao;

	@Autowired
	private SaccountRepository sdao;

	public static boolean isprimary(long account) {
		String s = Long.toString(account).substring(0, 10);
		String check = "10000000000";
		if (s.equals(check)) {
			return true;
		} else {
			return false;
		}

	}

	@GetMapping("/account/details/{account}")
	public Account getAccountDetails(@PathVariable("account") int account) {

		return service.getAccountDetails(account);

	}

	@PutMapping("/account/profile")
	public Account updateProfile(@RequestBody Account account) {
		return service.updateAccount(account);
	}

	@GetMapping("/account/getprimary/{username}")
	public Account getPrimarydetails(@PathVariable("username") String username) {
		return service.getAccount(username);
	}

	@GetMapping("/account/getsaving/{username}")
	public Saccount getSavingdetails(@PathVariable("username") String username) {
		return savservice.getAccount(username);
	}

	@PostMapping("/account/deposit")
	public DepositResponse deposit(@RequestBody TransactionDetails details) {
		if (isprimary(details.getAccount())) {
			return service.deposit(details.getAccount(), details.getAmount());
		} else {
			return savservice.deposit(details.getAccount(), details.getAmount());
		}
	}

	@PostMapping("/account/withdraw")
	public WithdrawResponse withdraw(@RequestBody TransactionDetails details) {

		if (isprimary(details.getAccount())) {
			return service.withdraw(details.getAccount(), details.getAmount());
		} else {
			return savservice.withdraw(details.getAccount(), details.getAmount());
		}
	}

	@PostMapping("/account/transfer")
	public TransferResponse transfer(@RequestBody TransferDetails details) {
		try {

			Account p = adao.findByUsername(details.getUsername());
			Saccount s = sdao.findByUsername(details.getUsername());

			if (p.getAccno() == details.getSaccount() || s.getAccno() == details.getSaccount()) {
				if (isprimary(details.getSaccount())) {
					return service.transfer(details.getSaccount(), details.getRaccount(), details.getAmount());
				} else {
					return savservice.transfer(details.getSaccount(), details.getRaccount(), details.getAmount());
				}
			} else {
				TransferResponse response = new TransferResponse();
				response.setSaccount(details.getSaccount());
				response.setResponseMessage("Transfer not Allowed!");
				response.setTransferStatus(false);
				return response;
			}

		} catch (Exception e) {
			System.out.println("Transfer exception: " + e);
			TransferResponse response = new TransferResponse();
			response.setSaccount(details.getSaccount());
			response.setResponseMessage("Transfer Unsuccessful. Please contact your bank");
			response.setTransferStatus(false);
			return response;

		}
	}

	@GetMapping("/account/getHistory/{account}")
	public List<UserHistory> getHistory(@PathVariable("account") long account) {
		List<UserHistory> history = histservice.getHistory(account);
		Collections.reverse(history);
		return history;
	}

	@GetMapping("/account/getTransfers/{account}")
	public List<Transfer> getTransfers(@PathVariable("account") long account) {
		return tservice.getTransfers(account);
	}

}
