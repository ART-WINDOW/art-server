package com.doma.artserver.service;

import org.springframework.data.domain.Page;

public interface MuseumExhibitionFacade<T> {
    public void loadData();
    public void updateData();
    Page<T> getExhibitions(int page, int pageSize);
    Page<T> getExhibitionsByArea(String area, int page, int pageSize);
    Page<T> getMuseums(int page, int pageSize);
    Page<T> getMajorExhibitions(int page, int pageSize);
    Page<T> searchExhibitions(String keyword, String area, int page, int pageSize);
    public void saveMajorMuseumsByNames();
    T getExhibitionById(Long id);
    T getMuseumById(Long id);
    void clearCache();
    void clearExhibition();
}
