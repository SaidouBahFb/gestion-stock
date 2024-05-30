package com.saidbah.gestionstockbac.repository;

import com.saidbah.gestionstockbac.entity.Company;
import com.saidbah.gestionstockbac.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findByCompany(Company company);

    boolean existsByEmail(String email);
}