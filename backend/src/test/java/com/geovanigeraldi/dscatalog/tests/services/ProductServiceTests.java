package com.geovanigeraldi.dscatalog.tests.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.geovanigeraldi.dscatalog.entities.Product;
import com.geovanigeraldi.dscatalog.repositories.ProductRepository;
import com.geovanigeraldi.dscatalog.services.ProductService;
import com.geovanigeraldi.dscatalog.services.exceptions.DataBaseException;
import com.geovanigeraldi.dscatalog.services.exceptions.ResourceNotFoundException;
import com.geovanigeraldi.dscatalog.tests.factories.ProductFactory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Long existingId;	
	private Long nonExistingId;
	private Long dependentId;
	private Product product;
	private PageImpl<Product> page;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = ProductFactory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.find(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any()))
				.thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);		
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});
	
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	
	}
		
}