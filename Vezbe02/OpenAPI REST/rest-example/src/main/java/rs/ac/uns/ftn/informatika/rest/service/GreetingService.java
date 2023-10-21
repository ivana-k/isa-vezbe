package rs.ac.uns.ftn.informatika.rest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import rs.ac.uns.ftn.informatika.rest.domain.Greeting;
import rs.ac.uns.ftn.informatika.rest.dto.GreetingDTO;
import rs.ac.uns.ftn.informatika.rest.dto.GreetingTextDTO;

public interface GreetingService {

	Collection<Greeting> findAll();

	Greeting findOne(Long id);

	Greeting create(GreetingDTO greeting) throws Exception;

	Greeting update(GreetingDTO greeting) throws Exception;

	Greeting delete(Long id);

	Greeting updateGreetingText(GreetingTextDTO greetingDTO, long id) throws Exception;

	ArrayList<Greeting> searchGreetings(Optional<String> text);
}