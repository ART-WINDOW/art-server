package com.doma.artserver.service;

import org.springframework.data.domain.Page;

public interface MuseumExhibitionFacade<T> {
    public void loadData();
    public void updateData();
    Page<T> getExhibitions(int page, int pageSize);
    Page<T> getMuseums(int page);
    T getExhibitionById(Long id);
    T getMuseumById(Long id);
}
