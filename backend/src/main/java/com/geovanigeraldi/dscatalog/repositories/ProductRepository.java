package com.geovanigeraldi.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.geovanigeraldi.dscatalog.entities.Category;
import com.geovanigeraldi.dscatalog.entities.Product;

//acesso aos dados do banco de dados (para todos os DB)
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query("select distinct prod from Product prod inner join prod.categories cats where "
			+ "(coalesce(:categories) is null OR cats in :categories) and "
			+ "(lower(prod.name) like lower(concat('%',:name,'%')))")
	Page<Product> find(List<Category> categories, String name, Pageable pageable);

}