package rs.ac.uns.ftn.informatika.inheritance.v1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

// ovde je dovoljno navesti da je klasa entity, sve se nasledjuje 
// iz BillingDetails
@Entity
@Table(name="v1_bankaccount")
public class BankAccount extends BillingDetails {

	@Column(name="number", unique=false, nullable=true)
	private String number;

	@Column(name="bank_name", unique=false, nullable=true)
	private String bankName;

	@Column(name="swift", unique=false, nullable=true)
	private String swift;

	public BankAccount() {
	}

	public BankAccount(String number, String bankName, String swift) {
		super();
		this.number = number;
		this.bankName = bankName;
		this.swift = swift;
	}



	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
	}
}
