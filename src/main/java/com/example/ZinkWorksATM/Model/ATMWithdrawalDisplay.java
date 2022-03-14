package com.example.ZinkWorksATM.Model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ATMWithdrawalDisplay implements Serializable{
	
	private static final long serialVersionUID = 2916998737027639857L;
	
	private Long accountNumber;
	private Integer pin;
	private Integer openingBalance;
	private Integer overDraft;
	private List<Bank> notes;
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
	public List<Bank> getNotes() {
		return notes;
	}
	public void setNotes(List<Bank> notes) {
		this.notes = notes;
	}
	public ATMWithdrawalDisplay() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ATMWithdrawalDisplay(Long accountNumber, Integer pin, Integer openingBalance, Integer overDraft, List<Bank> notes,String errorResponse) {
		super();
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.openingBalance = openingBalance;
		this.overDraft = overDraft;
		this.notes = notes;
		this.errorResponse = errorResponse;
	}

}
