package com.simplilearn.workshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simplilearn.workshop.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);
	public User findByEmail(String email);
	public User findByPhone(long l);
}
