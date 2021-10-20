package com.simplilearn.workshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplilearn.workshop.model.ChequebookRequest;

public interface ChequeBookRepository extends JpaRepository<ChequebookRequest, Integer>{

	public List<ChequebookRequest> findByAccount(long account);
}
