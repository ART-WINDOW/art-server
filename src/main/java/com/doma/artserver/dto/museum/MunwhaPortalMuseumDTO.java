package com.doma.artserver.dto.museum;

import com.doma.artserver.domain.museum.entity.Museum;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
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
