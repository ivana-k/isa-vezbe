package rs.ac.uns.ftn.informatika.inheritance.v1;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.SequenceGenerator;

/*
 * Prednosti strategije nasledjivanja gde se koristi po jedna tabela po konkretnoj klasi:
 * - svaka tabela stoji za sebe i nema suvisnih polja
 * - nema JOIN operacija ako se citaju atributi konkretne klase direktno
 * Mane strategije:
 * - problem vodjenja racuna o primarnim kljucevima (moraju biti jedinstveni na nivou baze,
 * 		a ne na nivou tabele)
 * - ako se menja roditeljska klasa (dodaje ili uklanja atribut), to znaci da se menjaju i
 * 		tabele koje predstavljaju klase naslednice jer se kolone iz roditeljske klase dupliraju
 * - pretraga nad kolonama roditeljske klase zahteva prolazak kroz sve tabele,
 * 		sto zahteva visestruke pristupe bazi
 */

@Entity
// ovom anotacijom se naglasava da je ova klasa koren hijerarhije koja koristi
// koncept jedna tabela po konkretnoj klasi
@Inheritance(strategy=TABLE_PER_CLASS)
public abstract class BillingDetails {

	@Id
	@SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
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
