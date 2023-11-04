package rs.ac.uns.ftn.informatika.cache.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.informatika.cache.domain.Product;
import rs.ac.uns.ftn.informatika.cache.service.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductsController {

	@Autowired
	private ProductService productService;

	@PostMapping(value = "/",
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
	@GetMapping(value = "/",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getProducts() {
		return productService.findAll();
	}
	@GetMapping(value = "/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Product getProduct(@PathVariable int id) {
		return productService.findOne(id);
	}
	@GetMapping(value = "/search/name/{name}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getProductByName(@PathVariable String name) {
		return productService.findByName(name);
	}
	@GetMapping(value = "/search/name/match/{name}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getOriginByNameMatch(@PathVariable String name) {
		return productService.findByNameMatch(name);
	}
	@GetMapping(value = "/search/param/{name}/{origin}/{price}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getProductByNamedParam(@PathVariable String name, @PathVariable String origin, @PathVariable long price) {
		return productService.findByNamedParam(name, origin, price);
	}

	@GetMapping(value = "/search/price/{price}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getProductByPrice(@PathVariable int price) {
		return productService.findByPrice(price);
	}
	@GetMapping(value = "/search/price/{price1}/{price2}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getProductByPriceRange(@PathVariable int price1, @PathVariable int price2) {
		return productService.findByPriceRange(price1, price2);
	}

	@GetMapping(value = "/removeCache")
	public ResponseEntity<String> removeFromCache() {
		productService.removeFromCache();
		return ResponseEntity.ok("Products successfully removed from cache!");
	}
}
