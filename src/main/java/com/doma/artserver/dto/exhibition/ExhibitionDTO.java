package com.doma.artserver.dto.exhibition;

import com.doma.artserver.domain.exhibition.entity.ExhibitionStatus;
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

    private String storageUrl;

    private String area;

    private LocalDate startDate;

    private LocalDate endDate;

    // 미술관/박물관 이름
    private String place;

    private Long apiId;

    private String url;

    // 전시 상태 (예정, 전시중, 종료)
    @Enumerated
    private ExhibitionStatus status;
}
