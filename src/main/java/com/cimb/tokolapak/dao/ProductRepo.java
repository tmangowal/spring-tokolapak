package com.cimb.tokolapak.dao;

import org.springframework.data.repository.CrudRepository;

import com.cimb.tokolapak.entity.Product;

public interface ProductRepo extends CrudRepository<Product, Integer> {
	public Product findByProductName(String productName);
}
