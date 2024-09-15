package com.doma.artserver.service.exhibition;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.domain.exhibition.repository.ExhibitionRepository;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public void processExhibitions() {
        int totalPages = apiClient.getTotalPages();

        for (int i = 1; i <= totalPages; i++) {
            List<MunwhaPortalExhibitionDTO> exhibitions = apiClient.fetchExhibitions(i);

            for (MunwhaPortalExhibitionDTO dto : exhibitions) {
                if ("미술".equals(dto.getRealmName())) {
                    saveExhibition(dto);
                }
            }
        }
    }

    @Override
    public void saveExhibition(MunwhaPortalExhibitionDTO dto) {
        Exhibition exhibition = exhibitionRepository.findByTitle(dto.getTitle());
        exhibition.setTitle(dto.getTitle());
        exhibition.setStartDate(dto.getStartDate());
        exhibition.setEndDate(dto.getEndDate());

        Museum museum = museumRepository.findByName(dto.getPlace());

        exhibition.setMuseum(museum);
        exhibitionRepository.save(exhibition);
    }

}
