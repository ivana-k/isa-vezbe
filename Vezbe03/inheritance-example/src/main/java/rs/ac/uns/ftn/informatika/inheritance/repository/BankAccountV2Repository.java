package rs.ac.uns.ftn.informatika.inheritance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.informatika.inheritance.v2.BankAccount;

public interface BankAccountV2Repository extends JpaRepository<BankAccount, Integer> {

}
