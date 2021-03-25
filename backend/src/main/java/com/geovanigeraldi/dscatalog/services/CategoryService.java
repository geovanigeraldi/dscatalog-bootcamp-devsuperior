package com.geovanigeraldi.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geovanigeraldi.dscatalog.dto.CategoryDTO;
import com.geovanigeraldi.dscatalog.entities.Category;
import com.geovanigeraldi.dscatalog.repositories.CategoryRepository;

@Service //registra como um componente gerenciado pelo spring
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x-> new CategoryDTO(x)).collect(Collectors.toList());
	}
	
}
