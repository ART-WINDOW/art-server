package com.doma.artserver.service.museum;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MuseumServiceImpl implements MuseumService {

    private final ApiClient apiClient;
    private final MuseumRepository museumRepository;

    public MuseumServiceImpl(ApiClient apiClient,
                             MuseumRepository museumRepository) {
        this.apiClient = apiClient;
        this.museumRepository = museumRepository;
    }

    @PostConstruct
    public void init() {
        processMuseum();
    }

    @Override
    public void processMuseum() {

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


