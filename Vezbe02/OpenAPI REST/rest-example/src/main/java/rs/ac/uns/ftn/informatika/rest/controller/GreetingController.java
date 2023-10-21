package rs.ac.uns.ftn.informatika.rest.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.informatika.rest.domain.Greeting;
import rs.ac.uns.ftn.informatika.rest.dto.GreetingDTO;
import rs.ac.uns.ftn.informatika.rest.dto.GreetingTextDTO;
import rs.ac.uns.ftn.informatika.rest.service.GreetingService;

/*
 * @RestController je anotacija nastala od @Controller tako da predstavlja bean komponentu.
 * 
 * @RequestMapping anotacija ukoliko se napise iznad kontrolera oznacava da sve rute ovog kontrolera imaju navedeni prefiks. 
 * U nasem primeru svaka rute kontrolera ima prefiks 'api/greetings'.
 *
 * OpenAPI dokumentacija se moze pogledati na: http://localhost:8080/swagger-ui/index.html
 */
@Tag(name = "Greeting controller", description = "The greeting API")
@RestController
@RequestMapping("/api/greetings")
public class GreetingController {

	@Autowired
	private GreetingService greetingService;

	/*
	 * Prilikom poziva metoda potrebno je navesti nekoliko parametara
	 * unutar @@GetMapping anotacije: url kao vrednost 'value' atributa (ukoliko se
	 * izostavi, ruta do metode je ruta do kontrolera), u slucaju GET zahteva
	 * atribut 'produce' sa naznakom tipa odgovora (u nasem slucaju JSON).
	 * 
	 * Kao povratna vrednost moze se vracati klasa ResponseEntity koja sadrzi i telo
	 * (sam podatak) i zaglavlje (metapodatke) i status kod, ili samo telo ako se
	 * metoda anotira sa @ResponseBody.
	 * 
	 * url: /api/greetings GET
	 */
	@Operation(summary = "Get all greetings", description = "Get all greetings", method="GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation",
					     content = @Content(mediaType = "application/json", 
					     array = @ArraySchema(schema = @Schema(implementation = Greeting.class))))
	})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Greeting>> getGreetings() {
		Collection<Greeting> greetings = greetingService.findAll();
		return new ResponseEntity<Collection<Greeting>>(greetings, HttpStatus.OK);
	}

	/*
	 * U viticastim zagradama se navodi promenljivi deo putanje.
	 * 
	 * url: /api/greetings/1 GET
	 */
	@Operation(summary = "Get greeting by id", description = "Get greeting by id", method="GET")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "found greeting by id",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = Greeting.class))),
			@ApiResponse(responseCode = "404", description = "greeting not found", content = @Content)
	})
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> getGreeting(@Parameter(name="id", description = "ID of a greeting to return", required = true) @PathVariable("id") Long id) {
		Greeting greeting = greetingService.findOne(id);

		if (greeting == null) {
			return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
	}

	/*
	 * Prilikom poziva metoda potrebno je navesti nekoliko parametara
	 * unutar @PostMapping anotacije: url kao vrednost 'value' atributa (ukoliko se
	 * izostavi, ruta do metode je ruta do kontrolera), u slucaju POST zahteva
	 * atribut 'produces' sa naznakom tipa odgovora (u nasem slucaju JSON) i atribut
	 * consumes' sa naznakom oblika u kojem se salje podatak (u nasem slucaju JSON).
	 * 
	 * Anotiranjem parametra sa @RequestBody Spring ce pokusati od prosledjenog JSON
	 * podatka da napravi objekat tipa Greeting.
	 * 
	 * url: /api/greetings POST
	 */
	@Operation(summary = "Create new greeting", description = "Create new greeting", method = "POST")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Created",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Greeting.class)) }),
			@ApiResponse(responseCode = "409", description = "Not possible to create new greeting when given id is not null",
					content = @Content)
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> createGreeting(@RequestBody GreetingDTO greeting)  {
		Greeting savedGreeting = null;
		try {
			savedGreeting = greetingService.create(greeting);
			return new ResponseEntity<Greeting>(savedGreeting, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Greeting>(savedGreeting, HttpStatus.CONFLICT);
		}
	}

	/*
	 * url: PUT /api/greetings/1
	 */
	@Operation(summary = "Update an existing greeting", description = "Update an existing greeting")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Greeting successfully edited",
					content =
							{ @Content(mediaType = "application/json", schema = @Schema(implementation = Greeting.class)) }
			),
			@ApiResponse(responseCode = "404", description = "Greeting not found", content = @Content)
			})
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> updateGreeting(@RequestBody GreetingDTO greeting) {
		try {
			Greeting updatedGreeting = greetingService.update(greeting);
			return new ResponseEntity<>(updatedGreeting, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@Operation(summary = "Update fields of an existing greeting", description = "Update fields of an existing greeting")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Greeting successfully edited",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Greeting.class)) }
			),
			@ApiResponse(responseCode = "404", description = "Greeting not found", content = @Content)})
	@PatchMapping (value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> updateGreetingText(@RequestBody GreetingTextDTO greetingDTO,
													   @Parameter(description = "Greeting id to update", required = true) @PathVariable long id) {
		Greeting updatedGreeting = null;
		try {
			updatedGreeting = greetingService.updateGreetingText(greetingDTO, id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Greeting>(updatedGreeting, HttpStatus.OK);
	}

	/*
	 * url: DELETE /api/greetings/1
	 */
	@Operation(summary = "Deletes a greeting", description = "Deletes a greeting", method = "DELETE")
	@ApiResponses(value = { @ApiResponse(responseCode = "404", description = "Greeting not found", content = @Content),
							@ApiResponse(responseCode = "204", description = "Greeting successfully deleted", content = @Content) } )
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Greeting> deleteGreeting(@Parameter(description = "Greeting id to delete", required = true) @PathVariable("id") Long id) {
		Greeting deletedGreeting = greetingService.delete(id);
		if (deletedGreeting == null) {
			return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);
	}

	/*
	  upotreba query parametra
	  GET /api/greetings/search?text=nekiTekst
	 */
	@Operation(summary = "Searches greetings by containing text", description = "Searches greetings by containing text", method = "GET")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of found greetings returned, empty if no results found",
			content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Greeting.class)))) } )
	@GetMapping(value = "/search")
	public ResponseEntity<Collection<Greeting>> searchGreetings(@Parameter(description = "Greeting text to search for")
														@RequestParam("text") Optional<String> text) {
		ArrayList<Greeting> foundGreetings = greetingService.searchGreetings(text);
		return new ResponseEntity<Collection<Greeting>>(foundGreetings, HttpStatus.OK);
	}

}
