package com.simplilearn.workshop.response;

public class TransferResponse {

	private boolean transferStatus;
	private String responseMessage;
	private long saccount;

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
		System.out.println(saccount);
		return saccount;
	}

	public void setSaccount(long saccount) {
		this.saccount = saccount;
		System.out.println("setSaccount +" + saccount);
	}

}
