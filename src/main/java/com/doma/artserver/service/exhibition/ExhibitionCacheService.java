package com.doma.artserver.service.exhibition;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;

import java.util.List;

public interface ExhibitionCacheService {
    void saveExhibition(ExhibitionDTO exhibitionDTO);
    ExhibitionDTO getExhibition(Long id);
    void deleteExhibition(Long id);
    void updateExhibition(ExhibitionDTO exhibitionDTO);
    void clearCache();
    public List<ExhibitionDTO> getExhibitions(int page, int pageSize);
}
