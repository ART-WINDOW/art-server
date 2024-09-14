package com.doma.artserver.domain.museum.repository;

import com.doma.artserver.domain.museum.entity.Museum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuseumRepository extends JpaRepository<Museum, Long> {
    Museum findByName(String place);
}
