package com.simplilearn.workshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simplilearn.workshop.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);
	public User findByEmail(String email);
	//public User findByPan(String pan);
	public User findByPhone(long l);
}