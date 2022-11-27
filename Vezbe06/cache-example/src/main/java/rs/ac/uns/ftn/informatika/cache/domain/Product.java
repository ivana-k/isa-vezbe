package rs.ac.uns.ftn.informatika.cache.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
/*
 * Primer upita koji se specificira nad samom klasom koja predstavlja tabelu u bazi
 */
@NamedQuery(name="Product.findByPrice", query="select p from Product p where p.price=?1")
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
	
	
	
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Product(String name, String origin, Long price) {
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
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
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
}
