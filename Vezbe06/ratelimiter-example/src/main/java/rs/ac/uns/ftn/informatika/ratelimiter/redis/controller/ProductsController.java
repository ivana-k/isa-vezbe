package rs.ac.uns.ftn.informatika.ratelimiter.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.domain.Product;
import rs.ac.uns.ftn.informatika.ratelimiter.redis.service.ProductService;

import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductsController {

	@Autowired
	private ProductService productService;

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Product addProduct(@RequestBody Product product){
		productService.saveProduct(product);
		return product;
	}

	@DeleteMapping(value = "/{id}")
	public void deleteProduct(@PathVariable int id) {
		productService.delete(id);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getProducts() {
		return productService.findAll();
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product getProduct(@PathVariable int id) {
		return productService.findOne(id);
	}

	@PutMapping(value = "/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
		return productService.updateOne(id, product);
	}

	@GetMapping(value = "/removeCache")
	public ResponseEntity<String> removeFromCache() {
		productService.removeFromCache();
		return ResponseEntity.ok("Products successfully removed from cache!");
	}
}
