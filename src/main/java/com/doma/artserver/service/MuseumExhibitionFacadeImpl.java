package com.doma.artserver.service;

import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import com.doma.artserver.dto.majormuseum.MajorMuseumDTO;
import com.doma.artserver.dto.museum.MuseumDTO;
import com.doma.artserver.service.exhibition.ExhibitionService;
import com.doma.artserver.service.majormuseum.MajorMuseumService;
import com.doma.artserver.service.museum.MuseumService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MuseumExhibitionFacadeImpl implements MuseumExhibitionFacade {

    private final MuseumService museumService;
    private final ExhibitionService exhibitionService;
    private final MajorMuseumService majorMuseumService;

    public MuseumExhibitionFacadeImpl(@Qualifier("museumServiceImpl") MuseumService museumService,
                                      @Qualifier("exhibitionServiceImpl") ExhibitionService exhibitionService,
                                      @Qualifier("majorMuseumServiceImpl") MajorMuseumService majorMuseumService) {
        this.museumService = museumService;
        this.exhibitionService = exhibitionService;
        this.majorMuseumService = majorMuseumService;
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
    public Page<ExhibitionDTO> getExhibitions(int page, int pageSize) {
        return exhibitionService.getExhibitions(page, pageSize);
    }

    @Override
    public Page<MuseumDTO> getMuseums(int page, int pageSize) {
        return museumService.getMuseums(page, pageSize);
    }

    @Override
    public Page<ExhibitionDTO> getMajorExhibitions(int page, int pageSize) {
        List<MajorMuseumDTO> majorMuseums = majorMuseumService.getMajorMuseums();

        // 각 MajorMuseumDTO의 museumId를 리스트로 변환
        List<Long> museumIds = majorMuseums.stream()
                .map(MajorMuseumDTO::getMuseumId)
                .toList();

        // museumIds를 사용하여 전시 목록을 가져옴
        Page<ExhibitionDTO> exhibitions = exhibitionService.getExhibitionsByMuseums(museumIds, page, pageSize);

        return exhibitions;
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
