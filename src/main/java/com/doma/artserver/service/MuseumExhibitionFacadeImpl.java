package com.doma.artserver.service;

import com.doma.artserver.config.MajorMuseumConfig;
import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import com.doma.artserver.dto.majormuseum.MajorMuseumDTO;
import com.doma.artserver.dto.museum.MuseumDTO;
import com.doma.artserver.service.exhibition.ExhibitionService;
import com.doma.artserver.service.exhibitiondetail.ExhibitionDetailServiceImpl;
import com.doma.artserver.service.majormuseum.MajorMuseumService;
import com.doma.artserver.service.museum.MuseumService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    private static final Logger logger = LoggerFactory.getLogger(MuseumExhibitionFacadeImpl.class);
    private final MuseumService museumService;
    private final ExhibitionService exhibitionService;
    private final MajorMuseumService majorMuseumService;
    private final CloseableHttpClient httpClient;
    private final MajorMuseumConfig majorMuseumConfig;
    private final ExhibitionDetailServiceImpl exhibitionDetailService;

    public MuseumExhibitionFacadeImpl(@Qualifier("museumServiceImpl") MuseumService museumService,
                                      @Qualifier("exhibitionServiceImpl") ExhibitionService exhibitionService,
                                      @Qualifier("majorMuseumServiceImpl") MajorMuseumService majorMuseumService,
                                      CloseableHttpClient httpClient,
                                      MajorMuseumConfig majorMuseumConfig,
                                      ExhibitionDetailServiceImpl exhibitionDetailService) {
        this.museumService = museumService;
        this.exhibitionService = exhibitionService;
        this.majorMuseumService = majorMuseumService;
        this.httpClient = httpClient;
        this.majorMuseumConfig = majorMuseumConfig;
        this.exhibitionDetailService = exhibitionDetailService;
    }

    @Override
    @Scheduled(cron = "0 0 4 * * ?")
    @CacheEvict(value = {"majorExhibitions", "majorMuseums"}, allEntries = true)
    public void loadData() {
        logger.info("Starting data load and evicting caches");
        museumService.fetchMuseum();
        exhibitionService.fetchExhibitions();
        exhibitionService.updateExhibitions();
        exhibitionDetailService.fetchExhibitionDetails();
        saveMajorMuseumsByNames();
        logger.info("Data load completed and caches evicted");
    }

    @Override
    @CacheEvict(value = {"majorExhibitions"}, allEntries = true)
    public void updateData() {
        exhibitionService.updateExhibitions();
    }

    public void clearCache() {
        // 캐시 관련 로직 제거
    }

    @EventListener
    public void handleMajorMuseumNamesUpdatedEvent(MajorMuseumConfig.MajorMuseumNamesUpdatedEvent event) {
        saveMajorMuseumsByNames(event.getNames());
    }

    @CacheEvict(value = {"majorMuseums", "majorExhibitions"}, allEntries = true)
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
    @Cacheable(value = "exhibitions", key = "#page + '-' + #pageSize")
    public Page<ExhibitionDTO> getExhibitions(int page, int pageSize) {
        return exhibitionService.getExhibitions(page, pageSize);
    }

    @Override
    @Cacheable(value = "exhibitionsByArea", key = "#area + '-' + #page + '-' + #pageSize")
    public Page<ExhibitionDTO> getExhibitionsByArea(String area, int page, int pageSize) {
        return exhibitionService.getExhibitionsByArea(area, page, pageSize);
    }

    @Override
    @Cacheable(value = "majorExhibitions", key = "#page + '-' + #pageSize")
    public Page<ExhibitionDTO> getMajorExhibitions(int page, int pageSize) {
        logger.info("Fetching major exhibitions from database for page: {}, size: {}", page, pageSize);
        List<MajorMuseumDTO> majorMuseums = majorMuseumService.getMajorMuseums();

        List<Long> museumIds = majorMuseums.stream()
                .map(MajorMuseumDTO::getMuseumId)
                .toList();

        return exhibitionService.getExhibitionsByMuseums(museumIds, page, pageSize);
    }

    @Override
    @Cacheable(value = "searchResults", key = "#keyword + '-' + #area + '-' + #page + '-' + #pageSize")
    public Page<ExhibitionDTO> searchExhibitions(String keyword, String area, int page, int pageSize) {
        logger.info("Searching exhibitions with keyword: {}, area: {}, page: {}, size: {}", keyword, area, page, pageSize);
        return exhibitionService.searchExhibitions(keyword, area, page, pageSize);
    }

    @Override
    @CacheEvict(value = {"majorMuseums", "majorExhibitions"}, allEntries = true)
    public void saveMajorMuseumsByNames() {
        logger.info("Saving major museums and evicting caches");
        List<String> museumNames = majorMuseumConfig.getNames();
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
        logger.info("Major museums saved and caches evicted");
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
    @Cacheable(value = "museums", key = "#page + '-' + #pageSize")
    public Page<MuseumDTO> getMuseums(int page, int pageSize) {
        return museumService.getMuseums(page, pageSize);
    }

    @Override
    public Object getExhibitionById(Long id) {
        return null;
    }

    @Override
    public Object getMuseumById(Long id) {
        return null;
    }

    @Override
    @CacheEvict(value = {"exhibitions", "exhibitionsByArea", "exhibitionsByMuseum", "searchResults", "majorExhibitions"}, allEntries = true)
    public void clearExhibition() {
        logger.info("Clearing all exhibitions and evicting all related caches");
        exhibitionService.clearExhibition();
    }
}
