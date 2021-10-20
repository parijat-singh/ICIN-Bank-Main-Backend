package com.simplilearn.workshop.model;

public class UserDisplay {
	String username;
	private long checkingAccno;
	private int checkingBalance;
	private long savingsAccno;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getPrimaryAccno() {
		return checkingAccno;
	}
	public void setPrimaryAccno(long primaryAccno) {
		this.checkingAccno = primaryAccno;
	}
	public int getPrimaryBalance() {
		return checkingBalance;
	}
	public void setPrimaryBalance(int primaryBalance) {
		this.checkingBalance = primaryBalance;
	}
	public long getSavingsAccno() {
		return savingsAccno;
	}
	public void setSavingsAccno(long savingsAccno) {
		this.savingsAccno = savingsAccno;
	}
	public int getSavingsBalance() {
		return savingsBalance;
	}
	public void setSavingsBalance(int savingsBalance) {
		this.savingsBalance = savingsBalance;
	}
	private int savingsBalance;
	public UserDisplay(String username, long primaryAccno, int primaryBalance, long savingsAccno, int savingsBalance) {
		super();
		this.username = username;
		this.checkingAccno = primaryAccno;
		this.checkingBalance = primaryBalance;
		this.savingsAccno = savingsAccno;
		this.savingsBalance = savingsBalance;
	}
	
	public UserDisplay() {
		// TODO Auto-generated constructor stub
	}
	
}
