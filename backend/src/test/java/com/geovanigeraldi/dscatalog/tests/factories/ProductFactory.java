package com.geovanigeraldi.dscatalog.tests.factories;

import java.time.Instant;

import com.geovanigeraldi.dscatalog.dto.ProductDTO;
import com.geovanigeraldi.dscatalog.entities.Product;

public class ProductFactory {

	public static Product createProduct() {
		return new Product(1L, "phone","good phone", 600.0,"https://img.com/img.png", Instant.parse("2021-05-05T10:53:00Z"));
	}

	public static ProductDTO createProductDTO() {
		return new ProductDTO(createProduct());
	}
}