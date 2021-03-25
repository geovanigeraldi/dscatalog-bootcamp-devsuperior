package com.geovanigeraldi.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.geovanigeraldi.dscatalog.entities.Category;

//acesso aos dados do banco de dados (para todos os DB)
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}
