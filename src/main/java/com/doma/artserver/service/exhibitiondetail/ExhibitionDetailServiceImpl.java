package com.doma.artserver.service.exhibitiondetail;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.detail.MunwhaExhibitionDetailDTO;
import com.doma.artserver.domain.exhibition.entity.Exhibition;
import com.doma.artserver.domain.exhibition.repository.ExhibitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExhibitionDetailServiceImpl {

    private final ApiClient<MunwhaExhibitionDetailDTO> apiClient;
    private final ExhibitionRepository exhibitionRepository;

    public ExhibitionDetailServiceImpl(ApiClient<MunwhaExhibitionDetailDTO> apiClient,
                                       ExhibitionRepository exhibitionRepository) {
        this.apiClient = apiClient;
        this.exhibitionRepository = exhibitionRepository;
    }

    @Transactional
    public void fetchExhibitionDetails() {
        System.out.println("전시 상세 데이터 수집 시작");
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        System.out.println("전시 수 : " + exhibitions.size());
        System.out.println(exhibitions.get(0).getApiId());

        for (Exhibition exhibition : exhibitions) {
            List<MunwhaExhibitionDetailDTO> detailList = apiClient.fetchItems(exhibition.getApiId());
                for (MunwhaExhibitionDetailDTO detail : detailList) {
                    exhibition.setUrl(detail.getUrl());
                    exhibition.setPrice(detail.getPrice());
                }
            exhibitionRepository.save(exhibition);
        }
    }



}
