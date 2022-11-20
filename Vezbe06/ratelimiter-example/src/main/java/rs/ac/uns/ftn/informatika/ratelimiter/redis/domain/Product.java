package rs.ac.uns.ftn.informatika.ratelimiter.redis.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "origin")
    private String origin;
    @Column(name = "price")
    private Long price;

    public Product() {
        super();
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