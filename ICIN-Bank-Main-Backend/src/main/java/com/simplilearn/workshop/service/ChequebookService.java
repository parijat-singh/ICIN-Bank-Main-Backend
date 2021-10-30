package com.simplilearn.workshop.service;

import java.util.List;

import com.simplilearn.workshop.model.ChequebookRequest;
import com.simplilearn.workshop.resp.ChequeResponse;

public interface ChequebookService {

	public ChequeResponse createrequest(ChequebookRequest chequebook);
	public List<ChequebookRequest> getRequests(long account);
}
