package com.doma.artserver.service.museum;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.MunwhaPortalApiClient;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
        int page = 1;  // 페이지 시작은 1부터
        List<MunwhaPortalMuseumDTO> museumList;

        // 받아온 리스트가 비어있지 않을 때까지 계속 반복
        do {
            museumList = apiClient.fetchMuseums(page);  // 페이지마다 데이터를 받아옴

            // 미술 카테고리만 필터링하여 저장
            museumList.stream()
                    .filter(dto -> "미술".equals(dto.getRealmName()))  // realmName이 "미술"인 항목만 필터링
                    .map(this::saeMuseum)                            // 저장 로직으로 전달
                    .forEach(museum -> System.out.println("Saved museum: " + museum.getName()));  // 저장된 미술관 정보 출력

            page++;  // 페이지 증가
        } while (!museumList.isEmpty());  // 리스트가 비어 있지 않으면 계속 반복
    }

    // 미술관 정보를 저장하는 메서드
    private Museum saveMuseum(MunwhaPortalMuseumDTO dto) {
        // DTO를 Entity로 변환
        Museum museum = dto.toEntity();

        // 이미 존재하는 미술관인지 체크 (중복 방지)
        return museumRepository.findByName(museum.getName())
                .orElseGet(() -> museumRepository.save(museum));  // 없다면 저장
    }

    } // processMuseum ends

}
