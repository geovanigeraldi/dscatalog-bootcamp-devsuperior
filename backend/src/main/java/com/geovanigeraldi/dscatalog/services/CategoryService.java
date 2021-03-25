package com.geovanigeraldi.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geovanigeraldi.dscatalog.entities.Category;
import com.geovanigeraldi.dscatalog.repositories.CategoryRepository;

@Service //registra como um componente gerenciado pelo spring
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<Category> findAll(){
		return repository.findAll();
	}
	
}
