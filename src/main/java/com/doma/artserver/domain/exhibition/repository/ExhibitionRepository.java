package com.doma.artserver.domain.exhibition.repository;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Optional<Exhibition> findByTitle(String title);
    Page<Exhibition> findAll(Pageable pageable);
    Page<Exhibition> findAllByOrderByStartDateDesc(Pageable pageable);

    // 전시중, 예정, 종료 순으로 정렬해서 받아오는 Query
    @Query("SELECT e FROM Exhibition e ORDER BY " +
            "CASE " +
            "WHEN e.status = 1 THEN 1 " + // ONGOING 상태
            "WHEN e.status = 0 THEN 2 " + // SCHEDULED 상태
            "WHEN e.status = 2 THEN 3 " + // COMPLETED 상태
            "END, e.startDate DESC")
    Page<Exhibition> findAllByStatusAndOrderByStartDate(Pageable pageable);

    // 박물관 id로 검색한 결과를 전시중, 예정, 종료 순으로 정렬해서 받아오는 Query
    @Query("SELECT e FROM Exhibition e WHERE e.museum.id IN :museumIds ORDER BY " +
            "CASE " +
            "WHEN e.status = 1 THEN 1 " + // ONGOING 상태
            "WHEN e.status = 0 THEN 2 " + // SCHEDULED 상태
            "WHEN e.status = 2 THEN 3 " + // COMPLETED 상태
            "END, e.startDate DESC")
    Page<Exhibition> findByMuseumIdsAndOrderByStatusAndStartDate(@Param("museumIds") List<Long> museumIds, Pageable pageable);


}
