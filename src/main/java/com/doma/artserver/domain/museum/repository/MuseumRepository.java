package com.doma.artserver.domain.museum.repository;

import com.doma.artserver.domain.museum.entity.Museum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MuseumRepository extends JpaRepository<Museum, Long> {
    Optional<Museum> findByName(String place);
}
