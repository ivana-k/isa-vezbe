package rs.ac.uns.ftn.informatika.inheritance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.informatika.inheritance.v2.CreditCard;

public interface CreditCardV2Repository extends JpaRepository<CreditCard, Integer> {

}
