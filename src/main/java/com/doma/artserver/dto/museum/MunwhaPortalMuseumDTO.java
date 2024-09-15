package com.doma.artserver.dto.museum;

import com.doma.artserver.domain.museum.entity.Museum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Setter @Getter
@XmlRootElement(name = "perforList")
@Builder
public class MunwhaPortalMuseumDTO {
    private String place;
    private String area;
    private String gpsX;
    private String gpsY;
    private String realmName;

    public Museum toEntity() {
        return Museum.builder()
                .name(this.place)
                .area(this.area)
                .gpsX(this.gpsX)
                .gpsY(this.gpsY)
                .build();
    }
}
