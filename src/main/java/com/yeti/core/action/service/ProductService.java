package com.yeti.core.action.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.action.ProductRepository;
import com.yeti.model.action.Product;
import com.yeti.model.util.Batch;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<Product>();
		productRepository.findAll().forEach(products::add);
		return products;
	}
	
	public Product getProduct(String id) {
		return productRepository.findOne(id);
	}
	
	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	public Product updateProduct(String id, Product product) {
		return productRepository.save(product);
	}

	public void deleteProduct(String id) {
		productRepository.delete(id);
	}
	
	public void processBatch(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return productRepository.exists(id);
	}
	
}
