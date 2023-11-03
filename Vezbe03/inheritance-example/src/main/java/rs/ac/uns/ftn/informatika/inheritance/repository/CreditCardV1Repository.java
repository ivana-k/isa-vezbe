package rs.ac.uns.ftn.informatika.inheritance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.informatika.inheritance.v1.CreditCard;

public interface CreditCardV1Repository extends JpaRepository<CreditCard, Integer> {

}
