package com.doma.artserver.api;

import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;

import java.util.List;

public interface ApiClient {
    List<MunwhaPortalExhibitionDTO> fetchExhibitions(int page);
    List<MunwhaPortalMuseumDTO> fetchMuseums(int page);
    String generateUrl(int page);
    int getTotalPages();
}
