package com.doma.artserver.domain.museum.repository;

import com.doma.artserver.domain.museum.entity.Museum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MuseumRepository extends JpaRepository<Museum, Long> {
    Optional<Museum> findByName(String place);
    Page<Museum> findAll(Pageable pageable);
    // 이름으로 여러 미술관을 검색하는 메소드
    List<Museum> findByNameIn(List<String> names);
}
