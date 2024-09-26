package com.doma.artserver.service.museum;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.MunwhaMuseumDTO;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.museum.MuseumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

        while(page < maxPage) {
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

    // 미구현
    @Override
    public Page<MuseumDTO> getMuseums(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Museum> museums = museumRepository.findAll(pageable);

        return new Page<MuseumDTO>() {
            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public <U> Page<U> map(Function<? super MuseumDTO, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<MuseumDTO> getContent() {
                return List.of();
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<MuseumDTO> iterator() {
                return null;
            }
        };
    }



} // MuseumService ends


