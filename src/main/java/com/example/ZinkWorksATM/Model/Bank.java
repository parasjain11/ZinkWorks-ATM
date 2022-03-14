package com.example.ZinkWorksATM.Model;

import java.io.Serializable;

public class Bank implements Serializable{

	private static final long serialVersionUID = 5548796160819642973L;
	private int value;
    private int amount;
    
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Bank(int value, int amount) {
		super();
		this.value = value;
		this.amount = amount;
	}
	public Bank() {
		super();
	}
}
