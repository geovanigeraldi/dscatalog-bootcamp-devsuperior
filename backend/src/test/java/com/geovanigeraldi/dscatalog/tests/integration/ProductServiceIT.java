package com.geovanigeraldi.dscatalog.tests.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.geovanigeraldi.dscatalog.dto.ProductDTO;
import com.geovanigeraldi.dscatalog.services.ProductService;
import com.geovanigeraldi.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;
		
	private Long existingId;	
	private Long nonExistingId;
	//private Long dependentId;
	private long countTotalProducts;
	private long countPCGamerProducts;
	private PageRequest pagerequest;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		//dependentId = 4L;
		countTotalProducts = 25L;
		countPCGamerProducts = 21L;
		pagerequest = PageRequest.of(0, 10);
	}
	
	//@Test
	//public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
	//	Assertions.assertThrows(DataBaseException.class, () -> {
	//		service.delete(dependentId);
	//	});
	//}
	
	@Test
	public void findAllPagedShouldReturnAllProductWhenNameIsEmpty() {
		String name = "";
				
		Page<ProductDTO> result = service.findAllPaged(0L, name, pagerequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnProductWhenNameExistsIgnoringCase() {
		String name = "pc gAmER";
		
		Page<ProductDTO> result = service.findAllPaged(0L, name, pagerequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, result.getTotalElements());
	}
	
	
	@Test
	public void findAllPagedShouldReturnProductWhenNameExists() {
		String name = "PC Gamer";
		
		Page<ProductDTO> result = service.findAllPaged(0L, name, pagerequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, result.getTotalElements());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}
	
}