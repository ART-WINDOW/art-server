package com.doma.artserver.service.exhibition;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import org.springframework.data.domain.Page;

public interface ExhibitionCacheService {
    void saveExhibition(ExhibitionDTO exhibitionDTO);
    ExhibitionDTO getExhibition(Long id);
    void deleteExhibition(Long id);
    void updateExhibition(ExhibitionDTO exhibitionDTO);
    void clearCache();
    public Page<ExhibitionDTO> getExhibitions(int page, int pageSize);
}
