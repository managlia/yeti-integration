package com.yeti.core.action.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.action.service.ProductService;
import com.yeti.model.action.Product;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Product.class)
@RequestMapping(value = "/Products", produces = "application/hal+json")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<List<Resource<Product>>> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		if( products != null ) {
			List<Resource<Product>> returnProducts = new ArrayList<Resource<Product>>();
			for( Product product : products ) {
				returnProducts.add(getProductResource(product));
			}
			return ResponseEntity.ok(returnProducts);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Product>> getProduct(@PathVariable String id) {
		Product product = productService.getProduct(id);
		if( product == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getProductResource(product));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<Product>> addProduct(@RequestBody Product product, HttpServletRequest request ) {
		Product newProduct = productService.addProduct(product);
		if( newProduct != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newProduct.getExternalId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<Product>> updateProduct(@RequestBody Product product, @PathVariable String id) {
		if( !productService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			productService.updateProduct(id, product);
			Product updatedProduct = productService.updateProduct(id, product);
			if( updatedProduct != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Product>> deleteProduct(@PathVariable String id) {
		if( !productService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			productService.deleteProduct(id);
			if( !productService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatch(@RequestBody Batch batch) {
		productService.processBatch(batch);
	}

	private Resource<Product> getProductResource(Product a) {
	    Resource<Product> resource = new Resource<Product>(a);
	    resource.add(linkTo(methodOn(ProductController.class).getProduct(a.getExternalId())).withSelfRel());
	    return resource;
	}

}








