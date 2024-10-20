package com.doma.artserver.dto.exhibition;

import com.doma.artserver.domain.exhibition.entity.ExhibitionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExhibitionDTO {
    private Long id;

    private String title;

    private String description;

    private String imgUrl;

    private String area;

    private LocalDate startDate;

    private LocalDate endDate;

    @JsonIgnore
    private byte[] imageData;

    // 미술관/박물관 이름
    private String place;

    // 전시 상태 (예정, 전시중, 종료)
    @Enumerated
    private ExhibitionStatus status;
}
