package com.doma.artserver.service.exhibition;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.MunwhaExhibitionDTO;
import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.domain.exhibition.repository.ExhibitionRepository;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ApiClient<MunwhaExhibitionDTO> apiClient;
    private final ExhibitionRepository exhibitionRepository;
    private final MuseumRepository museumRepository;

    public ExhibitionServiceImpl(ApiClient<MunwhaExhibitionDTO> apiClient, ExhibitionRepository exhibitionRepository, MuseumRepository museumRepository) {
        this.apiClient = apiClient;
        this.exhibitionRepository = exhibitionRepository;
        this.museumRepository = museumRepository;
    }

    @PostConstruct
    public void init() {
        fetchExhibitions();
    }

    @Override
    @Transactional
    public void fetchExhibitions() {
        int page = 1;

        while(true) {
            List<MunwhaExhibitionDTO> list = apiClient.fetchItems(page);
            if (list.isEmpty()) break;

            for (MunwhaExhibitionDTO dto : list) {
                Optional<Exhibition> existingExhibition = exhibitionRepository.findByTitle(dto.getTitle());

                if (existingExhibition.isEmpty()) {
                    Optional<Museum> museum = museumRepository.findByName(dto.getPlace());
                    if (museum.isPresent()) {
                        exhibitionRepository.save(dto.toEntity(museum.get()));
                    } else {
                        exhibitionRepository.save(dto.toEntity(Museum.builder().name("정보 없음").build()));
                    }
                }
            }
            page++;
        } // while ends

    } // fetchExhibitions() ends

    @Override
    public void saveExhibition(MunwhaPortalExhibitionDTO dto) {

    }

}
