package rs.ac.uns.ftn.informatika.inheritance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.informatika.inheritance.v3.BankAccount;

public interface BankAccountV3Repository extends JpaRepository<BankAccount, Integer> {

}
