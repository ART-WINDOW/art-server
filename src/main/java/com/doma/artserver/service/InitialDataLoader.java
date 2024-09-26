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
    private final MuseumRepository museumRepository;

    public InitialDataLoader(MuseumService museumService, ExhibitionService exhibitionService, MuseumRepository museumRepository) {
        this.museumService = museumService;
        this.exhibitionService = exhibitionService;
        this.museumRepository = museumRepository;
    }

    @PostConstruct
    public void init() {
        // 데이터가 비었을 경우 초기 로딩
        if (museumRepository.count() == 0) {
            System.out.println("DB가 비어있습니다. 전체 데이터를 저장합니다.");
            loadInitialData();
        } else {
            System.out.println("DB가 이미 업데이트 되었습니다.");
        }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void loadInitialData() {
        // 1. 먼저 전체 museum 데이터를 저장
        museumService.fetchMuseum();
        // 2. 저장된 museum 데이터를 기반으로 exhibition 데이터를 저장
        exhibitionService.fetchExhibitions();
    }
}
