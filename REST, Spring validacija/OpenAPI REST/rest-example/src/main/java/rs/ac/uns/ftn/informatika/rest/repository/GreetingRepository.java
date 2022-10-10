package rs.ac.uns.ftn.informatika.rest.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import rs.ac.uns.ftn.informatika.rest.domain.Greeting;

public interface GreetingRepository {

	Collection<Greeting> findAll();

	Greeting create(Greeting greeting);

	Greeting findOne(Long id);
	
	Greeting update(Greeting greeting);

	Greeting delete(Long id);

	ArrayList<Greeting> searchByText(Optional<String> text);
}
