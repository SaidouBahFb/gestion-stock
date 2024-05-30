package com.saidbah.gestionstockbac.repository;

import com.saidbah.gestionstockbac.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findById(Long id);
    boolean existsByEmail(String email);

    Optional<Company> findByEmail(String email);
    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c WHERE LOWER(c.name) = LOWER(:name)")
    boolean existsByName(String name);

}