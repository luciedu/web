package com.ipiecoles.communes.web.repository;

import com.ipiecoles.communes.web.model.Commune;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommuneRepository extends JpaRepository<Commune, String> {
    @Query("select count(distinct c.codePostal) from Commune c")
    long countDistinctCodePostal();

    List<Commune> findByLatitudeBetweenAndLongitudeBetween(Double latMin, Double latMax, Double longMin, Double longMax);

    Page<Commune> findByNomContainingIgnoreCase(String search, Pageable pageRequest);

}