package com.doma.artserver.service.exhibition;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.MunwhaExhibitionDTO;
import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.domain.exhibition.entity.ExhibitionStatus;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.domain.exhibition.repository.ExhibitionRepository;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.exhibition.ExhibitionDTO;
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

    private final ApiClient<MunwhaExhibitionDTO> apiClient;
    private final ExhibitionRepository exhibitionRepository;
    private final MuseumRepository museumRepository;
    private final ExhibitionCacheService exhibitionCacheService;

    public ExhibitionServiceImpl(ApiClient<MunwhaExhibitionDTO> apiClient,
                                 ExhibitionRepository exhibitionRepository,
                                 MuseumRepository museumRepository,
                                 ExhibitionCacheService exhibitionCacheService) {
        this.apiClient = apiClient;
        this.exhibitionRepository = exhibitionRepository;
        this.museumRepository = museumRepository;
        this.exhibitionCacheService = exhibitionCacheService;
    }

    @Override
    @Transactional
    public void fetchExhibitions() {
        int page = 1;
        int maxPage = 25;
        boolean isDbEmpty = exhibitionRepository.count() == 0;
        System.out.println("전시 수 : " + exhibitionRepository.count());

        if (!isDbEmpty) maxPage = 2;

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
        } // while ends

    } // fetchExhibitions() ends

    @Override
    public Page<ExhibitionDTO> getExhibitions(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Exhibition> exhibitions = exhibitionRepository.findAllByStatusAndOrderByStartDate(pageable);

        return exhibitions.map(this::convertToDTO);
    }

    @Override
    public Page<ExhibitionDTO> getExhibitionsByMuseums(List<Long> museumIds, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        for (Long id : museumIds) {
            System.out.println("Museum ID: " + id);
        }

        Page<Exhibition> exhibitions = exhibitionRepository.findByMuseumIdsAndOrderByStatusAndStartDate(museumIds, pageable);
        System.out.println("Exhibitions found: " + exhibitions.getTotalElements());

        exhibitions.forEach(exhibition -> System.out.println("Exhibition Title: " + exhibition.getTitle()));

        return exhibitions.map(this::convertToDTO);
    }

    // Exhibition -> ExhibitionDTO로 변환하는 메소드
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
                .build();
    }

    @Override
    @Transactional
    public void updateExhibitions() {
        List<Exhibition> exhibitions = exhibitionRepository.findAll();  // 모든 전시회 조회

        LocalDate today = LocalDate.now();

        for (Exhibition exhibition : exhibitions) {
            // 전시 상태 업데이트 로직
            if (exhibition.getStartDate().isAfter(today)) {
                exhibition.setStatus(ExhibitionStatus.SCHEDULED);  // 예정
            } else if ((exhibition.getStartDate().isBefore(today) || exhibition.getStartDate().isEqual(today)) && exhibition.getEndDate().isAfter(today)) {
                exhibition.setStatus(ExhibitionStatus.ONGOING);  // 진행 중
            } else if (exhibition.getEndDate().isBefore(today)) {
                exhibition.setStatus(ExhibitionStatus.COMPLETED);  // 종료
            }

            // 업데이트된 상태를 저장
            exhibitionRepository.save(exhibition);
        }
    }

    @Override
    public void cacheExhibitions() {
        exhibitionRepository.findAll().forEach(exhibition -> {
            ExhibitionDTO exhibitionDTO = convertToDTO(exhibition);
            exhibitionCacheService.saveExhibition(exhibitionDTO);
        });
    }

    @Override
    public void clearExhibition() {
        exhibitionRepository.deleteAll();
    }



}