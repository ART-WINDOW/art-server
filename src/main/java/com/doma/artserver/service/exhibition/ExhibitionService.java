package com.doma.artserver.service.exhibition;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExhibitionService {
    void fetchExhibitions();
    Page<ExhibitionDTO> getExhibitions(int page, int pageSize);
    Page<ExhibitionDTO> getExhibitionsByMuseums(List<Long> museumIds, int page, int pageSize);
    void updateExhibitions();
    void cacheExhibitions();
}
