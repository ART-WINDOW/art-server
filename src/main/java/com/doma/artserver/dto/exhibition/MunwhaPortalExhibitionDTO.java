package com.doma.artserver.dto.exhibition;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.domain.museum.entity.Museum;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Getter @Setter
@XmlRootElement(name = "perforList")
@Builder
public class MunwhaPortalExhibitionDTO {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String place;
    private String realmName;
    private String area;
    private String gpsX;
    private String gpsY;
    private String thumbnail;

    @XmlElement(name = "title")
    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "startDate")
    public void setStartDate(String startDate) {
        this.startDate = LocalDate.parse(startDate);
    }

    @XmlElement(name = "endDate")
    public void setEndDate(String endDate) {
        this.endDate = LocalDate.parse(endDate);
    }

    @XmlElement(name = "place")
    public void setPlace(String place) {
        this.place = place;
    }

    @XmlElement(name = "realmName")
    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    @XmlElement(name = "area")
    public void setArea(String area) {
        this.area = area;
    }

    @XmlElement(name = "gpsX")
    public void setGpsX(String gpsX) {
        this.gpsX = gpsX;
    }

    @XmlElement(name = "gpsY")
    public void setGpsY(String gpsY) {
        this.gpsY = gpsY;
    }

    @XmlElement(name = "thumbnail")
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Exhibition toEntity(Museum museum) {
        return Exhibition.builder()
                .title(this.title)
                .imgUrl(this.thumbnail)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .museum(museum)
                .build();
    }
}