package com.doma.artserver.service;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import com.doma.artserver.service.exhibition.ExhibitionService;
import com.doma.artserver.service.museum.MuseumService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MuseumExhibitionFacadeImpl implements MuseumExhibitionFacade {

    private final MuseumService museumService;
    private final ExhibitionService exhibitionService;

    public MuseumExhibitionFacadeImpl(@Qualifier("museumServiceImpl") MuseumService museumService,
                                      @Qualifier("exhibitionServiceImpl") ExhibitionService exhibitionService) {
        this.museumService = museumService;
        this.exhibitionService = exhibitionService;
    }

    @Override
    @PostConstruct
    @Scheduled(cron = "0 0 3 * * ?")
    public void loadData() {
        // 1. 먼저 전체 museum 데이터를 저장
        museumService.fetchMuseum();
        // 2. 저장된 museum 데이터를 기반으로 exhibition 데이터를 저장
        exhibitionService.fetchExhibitions();
    }

    @Override
    @PostConstruct
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateData() {
        // exhibition status 업데이트
        exhibitionService.updateExhibitions();
    }

    @Override
    public Page<ExhibitionDTO> getExhibitions(int page) {
        return exhibitionService.getExhibitions(page);
    }

    @Override
    public Page getMuseums(int page) {
        return null;
    }

    @Override
    public Object getExhibitionById(Long id) {
        return null;
    }

    @Override
    public Object getMuseumById(Long id) {
        return null;
    }


}
