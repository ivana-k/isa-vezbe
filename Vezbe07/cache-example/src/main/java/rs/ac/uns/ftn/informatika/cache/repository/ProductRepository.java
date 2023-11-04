package rs.ac.uns.ftn.informatika.cache.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.informatika.cache.domain.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
