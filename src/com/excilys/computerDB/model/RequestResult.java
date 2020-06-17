package com.excilys.computerDB.model;

public class RequestResult {
	private int status;
	private String result;
	
	public RequestResult() {
		this.status = -1;
		this.result = "";
	}
	
	public void reset() {
		this.status = -1;
		this.result = "";
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public void appendResult(String appendice) {
		this.result += appendice;
	}
	
	@Override
	public String toString() {
		return result;
	}
}
