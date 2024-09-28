package com.doma.artserver.domain.majormuseum.entity;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.domain.museum.entity.Museum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MajorMuseum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;  // 미술관 이름

    private String area;  // 미술관 지역

    private String gpsX; // 미술관 위치 X 좌표

    private String gpsY; // 미술관 위치 Y 좌표

    private String contactInfo;  // 연락처 정보

    private String website;  // 미술관 웹사이트 URL

    private Long museumId;
}
