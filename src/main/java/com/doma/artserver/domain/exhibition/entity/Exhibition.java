package com.doma.artserver.domain.exhibition.entity;

import com.doma.artserver.domain.museum.entity.Museum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter @Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String imgUrl;

    @Column(length = 1000)
    private String storageUrl;

    private String area;

    private LocalDate startDate;

    private LocalDate endDate;

    private String place;

    private String url;

    @Enumerated
    private ExhibitionStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    private Museum museum;

//    INST_NM	기관명
//    TITLE	제목
//    CATEGORY_NM	분류
//    URL	URL
//    IMAGE_URL	이미지URL
//    BEGIN_DE	시작일자
//    END_DE	종료일자
//    ADDR	주소
//    EVENT_TM_INFO	시간
//    PARTCPT_EXPN_INFO	비용
//    TELNO_INFO	전화번호
//    HOST_INST_NM	주최기관명
//    HMPG_URL	홈페이지URL
//    WRITNG_DE	작성일자

//    id	Long	자동 증가하는 기본 키
//    title	String	전시 제목 (텍스트 데이터)
//    description	String	전시 설명 (긴 텍스트)
//    location	String	전시 장소 (텍스트)
//    startDate	LocalDate	전시 시작일 (날짜)
//    endDate	LocalDate	전시 종료일 (날짜)
//    imageUrl	String	전시 이미지 URL
//    isActive	boolean	전시가 현재 활성화 상태인지 여부

}
