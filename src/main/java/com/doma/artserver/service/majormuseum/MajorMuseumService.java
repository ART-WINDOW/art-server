package com.doma.artserver.service.majormuseum;

import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;

import java.util.List;

public interface MajorMuseumService<T> {
    List<T> getMajorMuseums();
    void saveMajorMuseum(MajorMuseum majorMuseum);
}
