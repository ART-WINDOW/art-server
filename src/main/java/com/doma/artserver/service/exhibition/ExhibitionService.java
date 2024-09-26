package com.doma.artserver.service.exhibition;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import org.springframework.data.domain.Page;

public interface ExhibitionService<T> {
    void fetchExhibitions();
    Page<T> getExhibitions(int page);
    void updateExhibitions();
}
