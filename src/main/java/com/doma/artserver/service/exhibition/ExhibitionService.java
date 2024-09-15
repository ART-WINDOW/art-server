package com.doma.artserver.service.exhibition;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;

public interface ExhibitionService {
    void processExhibitions();
    void saveExhibition(MunwhaPortalExhibitionDTO dto);
}
