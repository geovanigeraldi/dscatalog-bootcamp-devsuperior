package com.geovanigeraldi.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geovanigeraldi.dscatalog.dto.CategoryDTO;
import com.geovanigeraldi.dscatalog.entities.Category;
import com.geovanigeraldi.dscatalog.repositories.CategoryRepository;
import com.geovanigeraldi.dscatalog.services.exceptions.EntityNotFoundException;

@Service //registra como um componente gerenciado pelo spring
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x-> new CategoryDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Optional<Category> obj = repository.findById(id);
		//Category entity = obj.get(); quando sempre achar o registro
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}
}
