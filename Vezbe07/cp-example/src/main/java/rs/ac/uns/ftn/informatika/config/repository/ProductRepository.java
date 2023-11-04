package rs.ac.uns.ftn.informatika.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.informatika.config.domain.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
