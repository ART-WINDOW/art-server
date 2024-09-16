package com.doma.artserver.api;

import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;

import java.net.URI;
import java.util.List;

public interface ApiClient {
    List<MunwhaPortalExhibitionDTO> fetchExhibitions(int page);
    List<MunwhaPortalMuseumDTO> fetchMuseums(int page);
    URI generateUrl(int page);
}
