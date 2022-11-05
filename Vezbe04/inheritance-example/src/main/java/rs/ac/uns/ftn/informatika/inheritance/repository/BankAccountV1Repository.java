package rs.ac.uns.ftn.informatika.inheritance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.informatika.inheritance.v1.BankAccount;

public interface BankAccountV1Repository extends JpaRepository<BankAccount, Integer> {

}
