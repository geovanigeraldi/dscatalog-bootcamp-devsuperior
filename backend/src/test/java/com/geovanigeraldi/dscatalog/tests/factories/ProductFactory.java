package com.geovanigeraldi.dscatalog.tests.factories;

import java.time.Instant;

import com.geovanigeraldi.dscatalog.dto.ProductDTO;
import com.geovanigeraldi.dscatalog.entities.Category;
import com.geovanigeraldi.dscatalog.entities.Product;

public class ProductFactory {

	public static Product createProduct() {
		Product product = new Product(1L, "phone","good phone", 600.0,"https://img.com/img.png", Instant.parse("2021-05-05T10:53:00Z"));
		product.getCategories().add(new Category(1L,null));
		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static ProductDTO createProductDTO(Long id) {
		ProductDTO dto = createProductDTO();
		dto.setId(id);
		return dto;
	}
	
}