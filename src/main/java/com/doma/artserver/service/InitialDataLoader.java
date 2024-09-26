package com.doma.artserver.service;

import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.service.exhibition.ExhibitionService;
import com.doma.artserver.service.museum.MuseumService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class InitialDataLoader {

    private final MuseumService museumService;
    private final ExhibitionService exhibitionService;

    public InitialDataLoader(MuseumService museumService, ExhibitionService exhibitionService, MuseumRepository museumRepository) {
        this.museumService = museumService;
        this.exhibitionService = exhibitionService;
    }

    @PostConstruct
    @Scheduled(cron = "0 0 3 * * ?")
    public void loadData() {
        // 1. 먼저 전체 museum 데이터를 저장
        museumService.fetchMuseum();
        // 2. 저장된 museum 데이터를 기반으로 exhibition 데이터를 저장
        exhibitionService.fetchExhibitions();
    }
}
