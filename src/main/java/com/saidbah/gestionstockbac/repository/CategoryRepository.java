package com.saidbah.gestionstockbac.repository;

import com.saidbah.gestionstockbac.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE LOWER(c.code) = LOWER(:code)")
    boolean existsByCode(String code);

    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE LOWER(c.designation) = LOWER(:designation)")
    boolean existsByDesignation(String designation);
}
