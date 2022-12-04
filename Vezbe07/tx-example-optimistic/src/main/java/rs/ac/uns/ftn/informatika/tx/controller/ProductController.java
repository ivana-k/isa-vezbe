package rs.ac.uns.ftn.informatika.tx.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.informatika.tx.domain.Product;
import rs.ac.uns.ftn.informatika.tx.service.ProductService;


@RestController
@RequestMapping(value = "/api")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping(value = "/products/",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Product addProduct(@RequestBody Product product){
		productService.save(product);
		return product;
	}

	@PutMapping(value = "/products/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> updateProduct(@RequestBody Product product) throws Exception {
		
		Product updatedProduct = null;
		try{
			updatedProduct = productService.update(product);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<Product>(HttpStatus.I_AM_A_TEAPOT); // :)
		}
		return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
	}
	@DeleteMapping(value = "/products/{id}")
	public void deleteProduct(@PathVariable int id) {
		productService.delete((long) id);
	}
	@GetMapping(value = "/products/")
	public List<Product> getProducts() {
		return productService.findAll();
	}
	@GetMapping(value = "/products/{id}")
	public Product getProduct(@PathVariable int id) {
		Product product = productService.findById((long) id);
		return product;
	}
}
