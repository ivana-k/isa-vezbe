package rs.ac.uns.ftn.informatika.inheritance.v3;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="v3_creditcard")
public class CreditCard extends BillingDetails {

	@Column(name="number", unique=false, nullable=true)
	private String number;

	@Column(name="exp_month", unique=false, nullable=true)
	private String expMonth;

	@Column(name="exp_year", unique=false, nullable=true)
	private String expYear;

	public CreditCard() {
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
