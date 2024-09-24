package com.doma.artserver.service.exhibition;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;

public interface ExhibitionService {
    void fetchExhibitions();
    void saveExhibition(MunwhaPortalExhibitionDTO dto);
}
