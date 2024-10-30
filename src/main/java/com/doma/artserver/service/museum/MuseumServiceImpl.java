package com.doma.artserver.service.museum;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.museum.MunwhaMuseumDTO;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.museum.MuseumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MuseumServiceImpl implements MuseumService {

    private final ApiClient<MunwhaMuseumDTO> apiClient;
    private final MuseumRepository museumRepository;

    public MuseumServiceImpl(ApiClient<MunwhaMuseumDTO> apiClient,
                             MuseumRepository museumRepository) {
        this.apiClient = apiClient;
        this.museumRepository = museumRepository;
    }

    @Override
    @Transactional
    public void fetchMuseum() {
        int page = 1;
        int maxPage = 12;

        boolean isDbEmpty = museumRepository.count() == 0;
        System.out.println("박물관 수 : " + museumRepository.count());

        if (!isDbEmpty) maxPage = 2;

        while (page < maxPage) {
            List<MunwhaMuseumDTO> list = apiClient.fetchItems(page);

            for (MunwhaMuseumDTO museumDTO : list) {
                // name (place) 값으로 중복 검사
                Optional<Museum> existingMuseum = museumRepository.findByName(museumDTO.getPlace());

                // 중복되지 않은 경우에만 저장
                if (existingMuseum.isEmpty()) {
                    museumRepository.save(museumDTO.toEntity());
                } else {
                    System.out.println("중복된 데이터: " + museumDTO.getPlace());
                }
            }
            page++;
        }

    }

    @Override
    public Page<MuseumDTO> getMuseums(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Museum> museums = museumRepository.findAll(pageable);

        return museums.map(this::convertToDTO);
    }

    @Override
    public List<Museum> findMuseumsByName(List names) {
        return museumRepository.findByNameIn(names);
    }

    // Exhibition -> ExhibitionDTO로 변환하는 메소드
    private MuseumDTO convertToDTO(Museum museum) {
        return MuseumDTO.builder()
                .name(museum.getName())
                .area(museum.getArea())
                .gpsX(museum.getGpsX())
                .gpsY(museum.getGpsY())
                .id(museum.getId())
                .contactInfo(museum.getContactInfo())
                .website(museum.getWebsite())
                .build();
    }


} // MuseumService ends


