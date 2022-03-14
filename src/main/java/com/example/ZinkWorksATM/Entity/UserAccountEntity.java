package com.example.ZinkWorksATM.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ACCOUNTS")
public class UserAccountEntity {
	
	@Id
	private Long accountNumber;
	private int pin;
	private int openingBalance;
	private int overDraft;
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public int getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(int openingBalance) {
		this.openingBalance = openingBalance;
	}
	public int getOverDraft() {
		return overDraft;
	}
	public void setOverDraft(int overDraft) {
		this.overDraft = overDraft;
	}
	public UserAccountEntity(Long accountNumber, int pin, int openingBalance, int overDraft) {
		super();
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.openingBalance = openingBalance;
		this.overDraft = overDraft;
	}
	public UserAccountEntity() {
		super();
	}

}
