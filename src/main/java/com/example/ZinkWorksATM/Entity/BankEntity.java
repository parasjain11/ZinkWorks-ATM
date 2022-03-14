package com.example.ZinkWorksATM.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BankEntity{
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;
	private int value;
    private int amount;
    
	public int getId() {
		return id;
	}
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
	public BankEntity(int value, int amount) {
		super();
		this.value = value;
		this.amount = amount;
	}
	public BankEntity() {
		super();
	}

}
