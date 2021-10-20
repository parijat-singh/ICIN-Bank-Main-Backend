package com.simplilearn.workshop.response;

public class TransferResponse {

	private boolean transferStatus;
	private String responseMessage;
	private long savingsaccount;
	
	public boolean isTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(boolean transferStatus) {
		this.transferStatus = transferStatus;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public long getSaccount() {
		return savingsaccount;
	}
	public void setSaccount(long savingsaccount) {
		this.savingsaccount = savingsaccount;
	}
	
	
}
