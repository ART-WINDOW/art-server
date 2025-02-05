package com.doma.artserver.service.exhibition;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.exhibition.MunwhaExhibitionDTO;
import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.domain.exhibition.entity.ExhibitionStatus;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.domain.exhibition.repository.ExhibitionRepository;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    private static final Logger logger = LoggerFactory.getLogger(ExhibitionServiceImpl.class);
    private static final int DEFAULT_MAX_PAGE = 25;
    private static final int NON_EMPTY_DB_MAX_PAGE = 2;

    private final ApiClient<MunwhaExhibitionDTO> apiClient;
    private final ExhibitionRepository exhibitionRepository;
    private final MuseumRepository museumRepository;
//    private final ExhibitionCacheService exhibitionCacheService;

    public ExhibitionServiceImpl(ApiClient<MunwhaExhibitionDTO> apiClient,
                                 ExhibitionRepository exhibitionRepository,
                                 MuseumRepository museumRepository
//                                 ,ExhibitionCacheService exhibitionCacheService
            )
     {
        this.apiClient = apiClient;
        this.exhibitionRepository = exhibitionRepository;
        this.museumRepository = museumRepository;
//        this.exhibitionCacheService = exhibitionCacheService;
    }

    @Override
    @Transactional
    public void fetchExhibitions() {
        fetchExhibitions(DEFAULT_MAX_PAGE);
    }

    @Transactional
    public void fetchExhibitions(int maxPage) {
        int page = 1;
        boolean isDbEmpty = exhibitionRepository.count() == 0;
        logger.info("전시 수 : {}", exhibitionRepository.count());

        if (!isDbEmpty) maxPage = NON_EMPTY_DB_MAX_PAGE;

        while (page < maxPage) {
            List<MunwhaExhibitionDTO> list = apiClient.fetchItems(page);

            for (MunwhaExhibitionDTO dto : list) {
                Optional<Exhibition> existingExhibition = exhibitionRepository.findByApiId(dto.getSeq());

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
        }
        System.out.println("전시 로딩 완료");
    }

    @Override
    public Page<ExhibitionDTO> getExhibitions(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Exhibition> exhibitions = exhibitionRepository.findAllByStatusAndOrderByStartDate(pageable);
        return exhibitions.map(this::convertToDTO);
    }

    @Override
    public Page<ExhibitionDTO> getExhibitionsByArea(String area, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Exhibition> exhibitions = exhibitionRepository.findByArea(area, pageable);
        return exhibitions.map(this::convertToDTO);
    }

    @Override
    public Page<ExhibitionDTO> getExhibitionsByMuseums(List<Long> museumIds, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Exhibition> exhibitions = exhibitionRepository.findByMuseumIdsAndOrderByStatusAndStartDate(museumIds, pageable);
        return exhibitions.map(this::convertToDTO);
    }

    @Override
    public Page<ExhibitionDTO> searchExhibitions(String keyword, String area, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Exhibition> exhibitions = exhibitionRepository.searchExhibitions(keyword, area, pageable);

        // 결과를 DTO로 변환하여 반환
        return exhibitions.map(this::convertToDTO);
    }

    private ExhibitionDTO convertToDTO(Exhibition exhibition) {
        return ExhibitionDTO.builder()
                .id(exhibition.getId())
                .title(exhibition.getTitle())
                .area(exhibition.getArea())
                .imgUrl(exhibition.getImgUrl())
                .place(exhibition.getPlace())
                .status(exhibition.getStatus())
                .startDate(exhibition.getStartDate())
                .endDate(exhibition.getEndDate())
                .storageUrl(exhibition.getStorageUrl())
                .apiId(exhibition.getApiId())
                .url(exhibition.getUrl())
                .price(exhibition.getPrice())
                .build();
    }

    @Override
    @Transactional
    public void updateExhibitions() {
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Exhibition exhibition : exhibitions) {
            updateExhibitionStatus(exhibition, today);
            exhibitionRepository.save(exhibition);
        }
    }

    private void updateExhibitionStatus(Exhibition exhibition, LocalDate today) {
        if (exhibition.getStartDate().isAfter(today)) {
            exhibition.setStatus(ExhibitionStatus.SCHEDULED);
        } else if ((exhibition.getStartDate().isBefore(today) || exhibition.getStartDate().isEqual(today)) && exhibition.getEndDate().isAfter(today)) {
            exhibition.setStatus(ExhibitionStatus.ONGOING);
        } else if (exhibition.getEndDate().isBefore(today)) {
            exhibition.setStatus(ExhibitionStatus.COMPLETED);
        }
    }

    @Override
    public void cacheExhibitions() {
//        exhibitionRepository.findAll().forEach(exhibition -> {
//            if (exhibition.getStatus() != ExhibitionStatus.COMPLETED) {
//                ExhibitionDTO exhibitionDTO = convertToDTO(exhibition);
//                exhibitionCacheService.saveExhibition(exhibitionDTO);
//            }
//        });
    }

    @Override
    public void clearExhibition() {
        exhibitionRepository.deleteAll();
    }
}