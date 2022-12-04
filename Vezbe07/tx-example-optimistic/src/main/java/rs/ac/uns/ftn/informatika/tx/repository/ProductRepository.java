package rs.ac.uns.ftn.informatika.tx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.informatika.tx.domain.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
