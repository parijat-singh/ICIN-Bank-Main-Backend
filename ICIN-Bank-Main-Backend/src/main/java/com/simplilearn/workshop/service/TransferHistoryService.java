package com.simplilearn.workshop.service;

import java.util.List;

import com.simplilearn.workshop.model.Transfer;

public interface TransferHistoryService {

	public Transfer addAction(long saccount, long raccount, int amount);
	public List<Transfer> getTransfers(long account);
}
