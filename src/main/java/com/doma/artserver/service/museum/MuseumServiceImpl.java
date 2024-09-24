package com.doma.artserver.service.museum;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.MunwhaMuseumDTO;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

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

    @PostConstruct
    public void init() {
        fetchMuseum();
    }

    @Override
    public void fetchMuseum() {
        int page = 1;
        while(page < 150) {
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

    // 미술관 정보를 저장하는 메서드
    private Museum saveMuseum(MunwhaPortalMuseumDTO dto) {
        // DTO를 Entity로 변환
        Museum museum = dto.toEntity();

        // 이미 존재하는 미술관인지 체크 (중복 방지)
        return museumRepository.findByName(museum.getName())
                .orElseGet(() -> museumRepository.save(museum));  // 없다면 저장
    }

} // MuseumService ends


