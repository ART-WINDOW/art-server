package com.doma.artserver.api.munhwa;

import com.doma.artserver.domain.museum.entity.Museum;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Getter
@Setter
@XmlRootElement(name = "perforList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MunwhaMuseumDTO {

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

    public Museum toEntity() {
        return Museum.builder()
                .name(this.place)
                .area(this.area)
                .gpsX(this.gpsX)
                .gpsY(this.gpsY)
                .build();
    }
}
