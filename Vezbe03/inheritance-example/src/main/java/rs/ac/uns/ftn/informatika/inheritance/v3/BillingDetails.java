package rs.ac.uns.ftn.informatika.inheritance.v3;

import static javax.persistence.InheritanceType.JOINED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * Prednosti strategije nasledjivanja gde se koristi po jedna tabela za svaki entitet:
 * - sve kolone su relevantne za svaku torku u tabeli, lakse su za razumevanje i nema bacanja prostora
 * - mapiranje modela na bazu je skoro 1 na 1 (svaka klasa ima svoju tabelu, svaki atribut ima svoju kolonu)
 * Mane strategije:
 * - da bi se ucitao objekat mora se koristiti vise tabela, sto znaci neizbeznu upotrebu JOINova
 * - roditeljska klasa moze biti usko grlo jer joj se precesto pristupa
 */

@Entity
@Table(name="v3_billingdetails")
// ovom anotacijom se naglasava mapiranje tipa "jedna tabela po svakoj klasi"
@Inheritance(strategy=JOINED)
public class BillingDetails {

	@Id
	@SequenceGenerator(name = "mySeqGenV3", sequenceName = "mySeqV3", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV3")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@Column(name="owner", unique=false, nullable=false)
	private String owner;

	public BillingDetails() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
