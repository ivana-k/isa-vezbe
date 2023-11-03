package rs.ac.uns.ftn.informatika.async.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.informatika.async.model.User;
import rs.ac.uns.ftn.informatika.async.service.EmailService;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/users")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private EmailService emailService;
	
	@GetMapping
	public String getNew(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}

	@PostMapping("/signup/async")
	public ResponseEntity<String> signUpAsync(@RequestBody User user){

		//slanje emaila
		try {
			System.out.println("Thread id: " + Thread.currentThread().getId());
			emailService.sendNotificaitionAsync(user);
		}catch( Exception e ){
			logger.info("Greska prilikom slanja emaila: " + e.getMessage());
		}

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	@PostMapping("/signup/sync")
	public ResponseEntity<String> signUpSync(@RequestBody User user){
		System.out.println("Thread id: " + Thread.currentThread().getId());
		//slanje emaila
		try {
			emailService.sendNotificaitionSync(user);
		}catch( Exception e ){
			logger.info("Greska prilikom slanja emaila: " + e.getMessage());
		}

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

}
