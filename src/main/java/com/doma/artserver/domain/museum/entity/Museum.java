package com.doma.artserver.domain.museum.entity;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Museum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 미술관의 고유 식별자

    private String name;  // 미술관 이름

    private String address;  // 미술관 주소

    private String contactInfo;  // 연락처 정보

    private String website;  // 미술관 웹사이트 URL

    // 1:N 관계 - 한 미술관에는 여러 전시회가 있을 수 있음
    @OneToMany(mappedBy = "artMuseum")
    private List<Exhibition> exhibitions;
}
