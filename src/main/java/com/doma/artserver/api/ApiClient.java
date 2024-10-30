package com.doma.artserver.api;

import com.doma.artserver.api.munhwa.detail.MunwhaExhibitionDetailDTO;

import java.net.URI;
import java.util.List;

public interface ApiClient<T> {
    List<T> fetchItems(int page);
    URI generateUrl(int page);
    List<T> fetchItems(Long apiId);
    URI generateUrl(Long seq);
}
