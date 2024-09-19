package com.doma.artserver.domain.museum.entity;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Museum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 미술관의 고유 식별자

    private String name;  // 미술관 이름

    private String area;  // 미술관 지역

    private String gpsX; // 미술관 위치 X 좌표

    private String gpsY; // 미술관 위치 Y 좌표

    private String contactInfo;  // 연락처 정보

    private String website;  // 미술관 웹사이트 URL

    // 1:N 관계 - 한 미술관에는 여러 전시회가 있을 수 있음
    @OneToMany(mappedBy = "museum")
    private List<Exhibition> exhibitions;
}
