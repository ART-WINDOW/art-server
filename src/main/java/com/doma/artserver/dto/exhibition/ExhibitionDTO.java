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

    private String area;

    private LocalDate startDate;

    private LocalDate endDate;

    private String place;

    @Enumerated
    private ExhibitionStatus status;
}
