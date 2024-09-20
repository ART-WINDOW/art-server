package com.doma.artserver.dto.museum;

import com.doma.artserver.domain.museum.entity.Museum;
import lombok.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@Setter
@XmlRootElement(name = "perforList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MunwhaPortalMuseumDTO {

    @XmlElement(name = "place")
    private String place;

    @XmlElement(name = "area")
    private String area;

    @XmlElement(name = "gpsX")
    private String gpsX;

    @XmlElement(name = "gpsY")
    private String gpsY;

    @XmlElement(name = "realmName")
    private String realmName;

    // 중복된 필드 또는 메서드가 있는지 확인
    // 만약 'getArea', 'getGpsX' 등과 같은 메서드가 중복되어 있다면 삭제 또는 이름 수정

    public Museum toEntity() {
        return Museum.builder()
                .name(this.place)
                .area(this.area)
                .gpsX(this.gpsX)
                .gpsY(this.gpsY)
                .build();
    }
}
