package com.dbs.demo.pcf.mysql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "credit_card")
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer number;

	@Column
	private String cardholderLastName;

	@Column
	private String cardholderFirstName;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getCardholderLastName() {
		return cardholderLastName;
	}

	public void setCardholderLastName(String cardholderLastName) {
		this.cardholderLastName = cardholderLastName;
	}

	public String getCardholderFirstName() {
		return cardholderFirstName;
	}

	public void setCardholderFirstName(String cardholderFirstName) {
		this.cardholderFirstName = cardholderFirstName;
	}
	
	

}
