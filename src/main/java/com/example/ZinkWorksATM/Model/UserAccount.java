package com.example.ZinkWorksATM.Model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccount implements Serializable{
	
	private static final long serialVersionUID = 6395926159896833732L;
	private Long accountNumber;
	private Integer pin;
	private Integer openingBalance;
	private Integer overDraft;
	private String errorResponse;
	
	
	public String getErrorResponse() {
		return errorResponse;
	}
	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}
	public Integer getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(Integer openingBalance) {
		this.openingBalance = openingBalance;
	}
	public Integer getOverDraft() {
		return overDraft;
	}
	public void setOverDraft(Integer overDraft) {
		this.overDraft = overDraft;
	}
	public UserAccount(Long accountNumber, Integer pin, Integer openingBalance, Integer overDraft, String errorResponse) {
		super();
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.openingBalance = openingBalance;
		this.overDraft = overDraft;
		this.errorResponse = errorResponse;
	}
}
