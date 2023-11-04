package rs.ac.uns.ftn.informatika.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import rs.ac.uns.ftn.informatika.config.domain.Product;
import rs.ac.uns.ftn.informatika.config.repository.ProductRepository;

/*
 * Anotacijama @Api, @ApiOperation, @ApiResponses moze se detaljno
 * dokumentovati ceo REST API.
 * Pristup JSON: http://localhost:8080/v2/api-docs
 * Pristup UI: http://localhost:8080/swagger-ui.html
 */
@RestController
@RequestMapping(value = "/api")
@Api(value = "products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepository;

	@PostMapping(value = "/products/",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a product resource.", notes = "Returns the product being saved.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created", response = Product.class),
	        @ApiResponse(code = 401, message = "Unauthorized"), 
	        @ApiResponse(code = 500, message = "Failure") })
	public Product addProduct(@ApiParam(value = "The product object", required = true) @RequestBody Product product){
		productRepository.save(product);
		return product;
	}
	@DeleteMapping(value = "/products/{id}")
	@ApiOperation(value = "Delete a product resource.", notes = "You have to provide a valid product ID in the URL. Once deleted the resource can not be recovered.", httpMethod = "DELETE")
	public void deleteProduct(@ApiParam(value = "The ID of the existing product resource.", required = true, example = "1") @PathVariable Long id) {
		productRepository.deleteById(id);
	}
	@GetMapping(value = "/products/")
	@ApiOperation(value = "Get a list of all products.", httpMethod = "GET")
	public List<Product> getProducts() {
		return productRepository.findAll();
	}
	@GetMapping(value = "/products/{id}")
	@ApiOperation(value = "Get a single product.", notes = "You have to provide a valid product ID.", httpMethod = "GET")
	public Product getProduct(@ApiParam(value = "The ID of the product.", required = true, example = "1") @PathVariable Long id) {
		Product product = productRepository.findById(id).get();
		return product;
	}
}
