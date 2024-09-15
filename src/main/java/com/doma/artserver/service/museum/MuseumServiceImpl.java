package com.doma.artserver.service.museum;

import com.doma.artserver.api.munhwa.MunwhaPortalApiClient;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class MuseumServiceImpl implements MuseumService {

    private final MunwhaPortalApiClient apiClient;
    private final MuseumRepository museumRepository;

    public MuseumServiceImpl(MunwhaPortalApiClient apiClient,
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
        int totalPage = apiClient.getTotalPages();

        // Stream을 사용하여 각 페이지의 데이터를 가져와 리스트에 추가
        List<MunwhaPortalMuseumDTO> museumList =
                IntStream.rangeClosed(1, totalPage)      // 1부터 totalPage까지의 숫자 스트림 생성
                        .mapToObj(apiClient::fetchMuseums)  // 각 페이지에 대해 fetchExhibitions 호출
                        .flatMap(List::stream)                 // 각 페이지의 리스트를 하나의 스트림으로 병합
                        .toList();         // 최종적으로 리스트로 변환

        for (MunwhaPortalMuseumDTO dto : museumList) {
            if (dto.getRealmName().equals("미술")) museumRepository.save(dto.toEntity());
        }

    } // processMuseum ends

}
