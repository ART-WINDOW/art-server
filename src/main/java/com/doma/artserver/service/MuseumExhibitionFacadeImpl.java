package com.doma.artserver.service;

import com.doma.artserver.config.MajorMuseumConfig;
import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import com.doma.artserver.dto.majormuseum.MajorMuseumDTO;
import com.doma.artserver.dto.museum.MuseumDTO;
import com.doma.artserver.service.exhibition.ExhibitionCacheService;
import com.doma.artserver.service.exhibition.ExhibitionService;
import com.doma.artserver.service.exhibitiondetail.ExhibitionDetailServiceImpl;
import com.doma.artserver.service.majormuseum.MajorMuseumService;
import com.doma.artserver.service.museum.MuseumService;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@EnableScheduling
public class MuseumExhibitionFacadeImpl implements MuseumExhibitionFacade {

    private final MuseumService museumService;
    private final ExhibitionService exhibitionService;
    private final MajorMuseumService majorMuseumService;
    private final CloseableHttpClient httpClient;
    private final ExhibitionCacheService exhibitionCacheService;
    private final MajorMuseumConfig majorMuseumConfig;
    private final ExhibitionDetailServiceImpl exhibitionDetailService;

    public MuseumExhibitionFacadeImpl(@Qualifier("museumServiceImpl") MuseumService museumService,
                                      @Qualifier("exhibitionServiceImpl") ExhibitionService exhibitionService,
                                      @Qualifier("majorMuseumServiceImpl") MajorMuseumService majorMuseumService,
                                      ExhibitionCacheService exhibitionCacheService,
                                      CloseableHttpClient httpClient,
                                      MajorMuseumConfig majorMuseumConfig,
                                      ExhibitionDetailServiceImpl exhibitionDetailService) {
        this.museumService = museumService;
        this.exhibitionService = exhibitionService;
        this.majorMuseumService = majorMuseumService;
        this.httpClient = httpClient;
        this.exhibitionCacheService = exhibitionCacheService;
        this.majorMuseumConfig = majorMuseumConfig;
        this.exhibitionDetailService = exhibitionDetailService;
    }

    @Override
    @Scheduled(cron = "0 0 4 * * ?")
    public void loadData() {
        // 1. 먼저 전체 museum 데이터를 저장
        museumService.fetchMuseum();
        // 2. 저장된 museum 데이터를 기반으로 exhibition 데이터를 저장
        exhibitionService.fetchExhibitions();
        // 3. exhibition status 업데이트
        exhibitionService.updateExhibitions();
        // 4. exhibition Detail 데이터 저장
        exhibitionDetailService.fetchExhibitionDetails();
        // 5. majorMuseum 갱신
        saveMajorMuseumsByNames();
        // 6. exhibition 데이터 cache에 저장
        exhibitionCacheService.clearCache();
        exhibitionService.cacheExhibitions();
        System.out.println("데이터 로드 완료");
    }

    @Override
//    @PostConstruct
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateData() {
        // exhibition status 업데이트
        exhibitionService.updateExhibitions();
        // exhibition 데이터 cache에 저장
        exhibitionCacheService.clearCache();
        exhibitionService.cacheExhibitions();
    }

    public void clearCache() {
        exhibitionCacheService.clearCache();
    }

    @EventListener
    public void handleMajorMuseumNamesUpdatedEvent(MajorMuseumConfig.MajorMuseumNamesUpdatedEvent event) {
        saveMajorMuseumsByNames(event.getNames());
    }

    public void saveMajorMuseumsByNames(List<String> museumNames) {
        List<Museum> museums = museumService.findMuseumsByName(museumNames);

        for (Museum museum : museums) {
            MajorMuseum majorMuseum = MajorMuseum.builder()
                    .name(museum.getName())
                    .area(museum.getArea())
                    .gpsX(museum.getGpsX())
                    .gpsY(museum.getGpsY())
                    .contactInfo(museum.getContactInfo())
                    .website(museum.getWebsite())
                    .museumId(museum.getId())
                    .build();

            majorMuseumService.saveMajorMuseum(majorMuseum);
        }
    }


    @Override
    public Page<ExhibitionDTO> getExhibitions(int page, int pageSize) {
        Page<ExhibitionDTO> exhibitions;

        // cache에 저장된 전시 목록이 있으면 cache에서 가져오고, 없으면 DB에서 가져와서 cache에 저장
        if (!exhibitionCacheService.getExhibitions(page, pageSize).isEmpty()) {
            exhibitions = exhibitionCacheService.getExhibitions(page, pageSize);
            for (ExhibitionDTO exhibition : exhibitions) {
                exhibitionCacheService.saveExhibition(exhibition);
            }
        } else {
            exhibitions = exhibitionService.getExhibitions(page, pageSize);
            for (ExhibitionDTO exhibition : exhibitions) {
                exhibitionCacheService.saveExhibition(exhibition);
            }
        }

        return exhibitions;
    }

    @Override
    public Page<ExhibitionDTO> getExhibitionsByArea(String area, int page, int pageSize) {
        Page<ExhibitionDTO> exhibitions;

        // cache에 저장된 전시 목록이 있으면 cache에서 가져오고, 없으면 DB에서 가져와서 cache에 저장
        if (!exhibitionCacheService.getExhibitionsByArea(area, page, pageSize).isEmpty()) {
            exhibitions = exhibitionCacheService.getExhibitionsByArea(area, page, pageSize);
            for (ExhibitionDTO exhibition : exhibitions) {
                exhibitionCacheService.saveExhibition(exhibition);
            }
        } else {
            exhibitions = exhibitionService.getExhibitionsByArea(area, page, pageSize);
            for (ExhibitionDTO exhibition : exhibitions) {
                exhibitionCacheService.saveExhibition(exhibition);
            }
        }

        return exhibitions;
    }

    // 전시 이미지 받아오기
    public byte[] fetchImageData(String imageUrl) {
        HttpGet request = new HttpGet(imageUrl);
        request.setHeader("User-Agent", "Mozilla/5.0");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println(entity);
                return EntityUtils.toByteArray(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
    public Page<ExhibitionDTO> searchExhibitions(String keyword, String area, int page, int pageSize) {
        return exhibitionService.searchExhibitions(keyword, area, page, pageSize);
    }

    @Override
    public void saveMajorMuseumsByNames() {
        List<String> museumNames = majorMuseumConfig.getNames();

        // 1. Museum 이름 리스트로 검색
        List<Museum> museums = museumService.findMuseumsByName(museumNames);

        // 2. 검색된 각 Museum 객체를 기반으로 MajorMuseum 생성 및 저장
        for (Museum museum : museums) {
            MajorMuseum majorMuseum = MajorMuseum.builder()
                    .name(museum.getName())
                    .area(museum.getArea())
                    .gpsX(museum.getGpsX())
                    .gpsY(museum.getGpsY())
                    .contactInfo(museum.getContactInfo())
                    .website(museum.getWebsite())
                    .museumId(museum.getId())
                    .build();

            majorMuseumService.saveMajorMuseum(majorMuseum);
        }
    }


    @Override
    public Object getExhibitionById(Long id) {
        return null;
    }

    @Override
    public Object getMuseumById(Long id) {
        return null;
    }

    public void clearExhibition() {
        exhibitionService.clearExhibition();
    }


}
