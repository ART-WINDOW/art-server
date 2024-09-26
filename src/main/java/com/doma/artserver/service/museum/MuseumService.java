package com.doma.artserver.service.museum;

import org.springframework.data.domain.Page;

import java.util.List;

public interface MuseumService<T> {
    void fetchMuseum();
    Page<T> getMuseums(int page);
}
