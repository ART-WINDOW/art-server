package com.doma.artserver.domain.exhibition.repository;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    Exhibition findByTitle(String title);
}
