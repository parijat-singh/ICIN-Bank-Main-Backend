package com.simplilearn.workshop.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simplilearn.workshop.model.Transfer;
import com.simplilearn.workshop.repository.TransferHistoryRepository;
import com.simplilearn.workshop.service.TransferHistoryService;

@Service
public class TransferHistoryServiceImpl implements TransferHistoryService{

	@Autowired
	private TransferHistoryRepository thrdata;
	
	@Override
	public Transfer addAction(long saccount, long raccount, int amount) {
		LocalDate today = LocalDate.now();
		Transfer transfer=new Transfer();
		transfer.setSaccount(saccount);
		transfer.setRaccount(raccount);
		transfer.setAmount(amount);
		transfer.setDate(today);
		return thrdata.save(transfer);
	}

	@Override
	public List<Transfer> getTransfers(long account) {
		List<Transfer> sender=thrdata.findBySaccount(account);
		List<Transfer> receiver=thrdata.findByRaccount(account);
		List<Transfer> merged=new ArrayList<>();
		merged.addAll(sender);
		merged.addAll(receiver);
		Collections.sort(merged);
		return merged;
	}

}
