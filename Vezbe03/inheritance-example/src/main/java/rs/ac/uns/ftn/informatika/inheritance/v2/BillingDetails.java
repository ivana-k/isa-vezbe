package rs.ac.uns.ftn.informatika.inheritance.v2;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * Prednosti strategije nasledjivanja gde se koristi jedna tabela za sve entitete:
 * - postoji samo jedna tabela :)
 * - nema JOIN operacija za pribavljanje podataka
 * - bilo kakvo dodavanje novih atributa u bilo koju od klasa modela nasledjivanja ne zahteva promenu baze
 * 
 * Mane strategije:
 * - ako neko koristi tabelu direktno, van koda, moze biti zbunjujuce jer ce neke kolone biti relevantne,
 * 		a neke ne (cak ako neka kolona pripada klasi naslednici a ima null vrednost moze uneti zabunu)
 * - potencijalno previse null vrednosti za tudja obelezja (bespotrebno koriscenje prostora),
 * 		ali baze imaju mehanizme za ustedu bacenog prostora, pogotovo za opcione kolone koje se nalaze sa desne strane tabela
 * - jedan tabela moze postati prevelika, cesto joj se pristupa i cesto se zakljucava,
 * 		pa to moze uticati na performanse
 * - postoje iskljucivo jedinstveni nazivi za kolone u toj jednoj tabeli,
 * 		pa se mora voditi racuna da kolone na koje se mapiraju atributi razlicitih klasa naslednica
 * 		ne imenuju isto
 */

@Entity
@Table(name="v2_billingdetails")
// ovom anotacijom se naglasava tip mapiranja "jedna tabela po hijerarhiji"
@Inheritance(strategy=SINGLE_TABLE)
// ovom anotacijom se navodi diskriminatorska kolona
@DiscriminatorColumn(name="type", discriminatorType=STRING)
public abstract class BillingDetails {

	@Id
	@SequenceGenerator(name = "mySeqGenV2", sequenceName = "mySeqV2", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV2")
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
