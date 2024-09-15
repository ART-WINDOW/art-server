package com.doma.artserver.service;

import com.doma.artserver.api.munhwa.MunwhaPortalApiClient;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.service.museum.MuseumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MuseumServiceImplTest {

    @Mock
    private MunwhaPortalApiClient apiClient;

    @InjectMocks
    private MuseumServiceImpl museumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processMuseumTest() {
        // mock 전시 정보 생성
        MunwhaPortalExhibitionDTO exhibition1 = MunwhaPortalExhibitionDTO.builder()
                .title("전시1")
                .place("미술관1")
                .build();
        MunwhaPortalExhibitionDTO exhibition2 = MunwhaPortalExhibitionDTO.builder()
                .title("전시2")
                .place("미술관2")
                .build();


    }


}
