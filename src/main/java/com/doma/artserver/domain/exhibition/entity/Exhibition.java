package com.doma.artserver.domain.exhibition.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


}
