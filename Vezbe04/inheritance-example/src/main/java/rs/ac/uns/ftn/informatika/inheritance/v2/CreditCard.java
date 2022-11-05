package rs.ac.uns.ftn.informatika.inheritance.v2;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
// ovom anotacijom se navodi vrednost diskriminatorske kolone koja vazi za 
// objekte ove klase
@DiscriminatorValue("CC")
public class CreditCard extends BillingDetails {

	@Column(name="cc_number", unique=false, nullable=true)
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
