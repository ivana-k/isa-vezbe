package rs.ac.uns.ftn.informatika.inheritance.v1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

// ovde je dovoljno navesti da je klasa entity, sve se nasledjuje 
// iz BillingDetails
@Entity
@Table(name="v1_creditcard")
public class CreditCard extends BillingDetails {

	@Column(name="number", unique=false, nullable=true)
	private String number;

	@Column(name="exp_month", unique=false, nullable=true)
	private String expMonth;

	@Column(name="exp_year", unique=false, nullable=true)
	private String expYear;

	public CreditCard() {
	}


	public CreditCard(String number, String expMonth, String expYear) {
		super();
		this.number = number;
		this.expMonth = expMonth;
		this.expYear = expYear;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}
}