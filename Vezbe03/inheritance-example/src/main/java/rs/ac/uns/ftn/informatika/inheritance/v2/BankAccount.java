package rs.ac.uns.ftn.informatika.inheritance.v2;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//ovom anotacijom se navodi vrednost diskriminatorske kolone koja vazi za 
//objekte ove klase
@DiscriminatorValue("BA")
public class BankAccount extends BillingDetails {

	@Column(name="ba_number", unique=false, nullable=true)
	private String number;

	@Column(name="bank_name", unique=false, nullable=true)
	private String bankName;

	@Column(name="swift", unique=false, nullable=true)
	private String swift;

	public BankAccount() {
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
