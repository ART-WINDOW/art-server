package com.doma.artserver.domain.majormuseum.repository;

import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MajorMuseumRepository extends JpaRepository<MajorMuseum, Long> {
    List<MajorMuseum> findAll();
    Optional<MajorMuseum> findByName(String name);
}
