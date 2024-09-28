package com.doma.artserver.domain.majormuseum.repository;

import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorMuseumRepository extends JpaRepository<MajorMuseum, Long> {
    List<MajorMuseum> findAll();
}
