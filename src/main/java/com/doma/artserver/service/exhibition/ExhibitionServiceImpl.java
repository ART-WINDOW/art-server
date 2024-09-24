package com.doma.artserver.service.exhibition;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.domain.exhibition.repository.ExhibitionRepository;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ApiClient apiClient;
    private final ExhibitionRepository exhibitionRepository;
    private final MuseumRepository museumRepository;

    public ExhibitionServiceImpl(ApiClient apiClient, ExhibitionRepository exhibitionRepository, MuseumRepository museumRepository) {
        this.apiClient = apiClient;
        this.exhibitionRepository = exhibitionRepository;
        this.museumRepository = museumRepository;
    }

    @Override
    @Transactional
    public void fetchExhibitions() {

    }

    @Override
    public void saveExhibition(MunwhaPortalExhibitionDTO dto) {
        Exhibition exhibition = exhibitionRepository.findByTitle(dto.getTitle());
        exhibition.setTitle(dto.getTitle());
        exhibition.setStartDate(dto.getStartDate());
        exhibition.setEndDate(dto.getEndDate());

        Optional<Museum> museum = museumRepository.findByName(dto.getPlace());

        exhibition.setMuseum(museum.get());
        exhibitionRepository.save(exhibition);
    }

}
