package rs.ac.uns.ftn.informatika.tx.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="name")
	private String name;
	@Column(name="origin")
	private String origin;
	@Column(name="price")
	private Long price;
	/*
	 * Za primer optimistickog zakljucavanja, Spring i EJB koriste posebnu anotaciju
	 * @Version kojom se anotira obicno integer polje koje se pri svakoj promeni entiteta
	 * povecava za 1. Svaki klijent ce dobiti i informaciju o verziji podatka.
	 * Prilikom izmene podatka potrebno je proveriti da li je podatke neko drugi u medjuvremenu menjao:
	 * - poredi se verzija podatka koju je klijent procitao sa onim sto se trenutno nalazi u bazi
	 * - poredjenje se vrsi pri commit-u transakcije (normal validation)
	 *   ili pri svakom pisanju u bazu u toku transakcije (early validation)
	 * - ako su podaci menjani prijavljuje se greska korisniku
	 */
	@Version
	private Integer version;
	
	public Product() {
		
	}
	
	public Product(String name, String origin, Long price) {
		super();
		this.name = name;
		this.origin = origin;
		this.price = price;
	}
	

	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", origin=" + origin + ", price=" + price + "]";
	}
	
}

