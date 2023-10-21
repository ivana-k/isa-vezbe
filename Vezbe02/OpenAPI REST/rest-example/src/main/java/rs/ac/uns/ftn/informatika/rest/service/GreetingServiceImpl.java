package rs.ac.uns.ftn.informatika.rest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.informatika.rest.domain.Greeting;
import rs.ac.uns.ftn.informatika.rest.dto.GreetingDTO;
import rs.ac.uns.ftn.informatika.rest.dto.GreetingTextDTO;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryGreetingRepository;

@Service
public class GreetingServiceImpl implements GreetingService {

	@Autowired
	private InMemoryGreetingRepository greetingRepository;

	@Override
	public Collection<Greeting> findAll() {
		Collection<Greeting> greetings = greetingRepository.findAll();
		return greetings;
	}

	@Override
	public Greeting findOne(Long id) {
		Greeting greeting = greetingRepository.findOne(id);
		return greeting;
	}

	@Override
	public Greeting create(GreetingDTO greeting) throws Exception {
		if (greeting.getId() != null) {
			throw new Exception("Id mora biti null prilikom perzistencije novog entiteta.");
		}
		Greeting savedGreeting = greetingRepository.create(new Greeting(greeting));
		return savedGreeting;
	}

	@Override
	public Greeting update(GreetingDTO greeting) throws Exception {
		Greeting greetingToUpdate = findOne(greeting.getId());
		if (greetingToUpdate == null) {
			throw new Exception("Trazeni entitet nije pronadjen.");
		}
		greetingToUpdate.setText(greeting.getText());
		greetingToUpdate.setAuthor(greeting.getAuthor());
		Greeting updatedGreeting = greetingRepository.create(greetingToUpdate);
		return updatedGreeting;
	}

	@Override
	public Greeting delete(Long id) {
		return greetingRepository.delete(id);
	}

	@Override
	public Greeting updateGreetingText(GreetingTextDTO greetingDTO, long greetingId) throws Exception {
		Greeting greetingToUpdate = findOne(greetingId);
		if (greetingToUpdate == null) {
			throw new Exception("Trazeni entitet nije pronadjen.");
		}

		greetingToUpdate.setText(greetingDTO.getText());
		return greetingToUpdate;
	}

	@Override
	public ArrayList<Greeting> searchGreetings(Optional<String> text) {
		ArrayList<Greeting> foundGreetings = greetingRepository.searchByText(text);
		return foundGreetings;
	}

}
