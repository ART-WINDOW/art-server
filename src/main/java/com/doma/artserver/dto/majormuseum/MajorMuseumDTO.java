package com.doma.artserver.dto.majormuseum;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorMuseumDTO {
    private Long id;

    private String name;  // 미술관 이름

    private String area;  // 미술관 지역

    private String gpsX; // 미술관 위치 X 좌표

    private String gpsY; // 미술관 위치 Y 좌표

    private String contactInfo;  // 연락처 정보

    private String website;  // 미술관 웹사이트 URL

    private Long museumId;
}
