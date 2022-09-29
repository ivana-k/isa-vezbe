package rs.ac.uns.ftn.informatika.rest.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.informatika.rest.domain.Greeting;

@Repository
public class InMemoryGreetingRepository implements GreetingRepository {

	private static AtomicLong counter = new AtomicLong();

	private final ConcurrentMap<Long, Greeting> greetings = new ConcurrentHashMap<Long, Greeting>();

	@Override
	public Collection<Greeting> findAll() {
		return this.greetings.values();
	}

	@Override
	public Greeting create(Greeting greeting) {
		Long id = greeting.getId();

		if (id == null) {
			id = counter.incrementAndGet();
			greeting.setId(id);
		}

		this.greetings.put(id, greeting);
		return greeting;
	}

	@Override
	public Greeting findOne(Long id) {
		return this.greetings.get(id);
	}

	@Override
	public Greeting delete(Long id) {
		Greeting greeting = this.greetings.remove(id);
		return greeting;
	}

	@Override
	public ArrayList<Greeting> searchByText(Optional<String> text) {

		ArrayList<Greeting> foundGreetings = (ArrayList<Greeting>) this.greetings.values().stream()
				.filter(x -> x.getText().toLowerCase().contains(text.get().toLowerCase()))
				.collect(Collectors.toList());
		return foundGreetings;
	}

	@Override
	public Greeting update(Greeting greeting) {
		Long id = greeting.getId();

		if (id != null) {
			this.greetings.put(id, greeting);
		}

		return greeting;
	}

}
