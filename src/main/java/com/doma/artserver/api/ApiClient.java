package com.doma.artserver.api;

import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;

import java.util.List;

public interface ApiClient {
    List<MunwhaPortalExhibitionDTO> fetchExhibitions(int page);
    String generateUrl(int page);
    int getTotalPages();
}
