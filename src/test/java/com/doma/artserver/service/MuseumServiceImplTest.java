package com.doma.artserver.service;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.MunwhaMuseumDTO;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.museum.MuseumDTO;
import com.doma.artserver.service.museum.MuseumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MuseumServiceImplTest {

    @Mock
    private ApiClient<MunwhaMuseumDTO> apiClient;

    @Mock
    private MuseumRepository museumRepository;

    @InjectMocks
    private MuseumServiceImpl museumService;

    @BeforeEach
    public void setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchMuseum() {
        // Given
        MunwhaMuseumDTO museumDTO1 = new MunwhaMuseumDTO();
        museumDTO1.setPlace("Museum A");
        MunwhaMuseumDTO museumDTO2 = new MunwhaMuseumDTO();
        museumDTO2.setPlace("Museum B");

        when(apiClient.fetchItems(1)).thenReturn(Arrays.asList(museumDTO1, museumDTO2));
        when(museumRepository.findByName("Museum A")).thenReturn(Optional.empty());
        when(museumRepository.findByName("Museum B")).thenReturn(Optional.empty());

        // When
        museumService.fetchMuseum();

        // Then
        verify(museumRepository, times(2)).save(any(Museum.class)); // 중복 체크 후 저장 확인
    }

    @Test
    public void testGetMuseums() {
        // Given
        Museum museum1 = Museum.builder()
                .name("Museum A")
                .area("Seoul")
                .id(1L)
                .build();

        Museum museum2 = Museum.builder()
                .name("Museum B")
                .area("Busan")
                .id(2L)
                .build();

        List<Museum> museumList = Arrays.asList(museum1, museum2);
        Page<Museum> museumPage = new PageImpl<>(museumList);

        Pageable pageable = PageRequest.of(0, 10);

        when(museumRepository.findAll(pageable)).thenReturn(museumPage);

        // When
        Page<MuseumDTO> result = museumService.getMuseums(0, 10);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals("Museum A", result.getContent().get(0).getName());
        assertEquals("Museum B", result.getContent().get(1).getName());
    }

    @Test
    public void testFindMuseumsByName() {
        // Given
        Museum museum1 = Museum.builder()
                .name("Museum A")
                .area("Seoul")
                .id(1L)
                .build();

        Museum museum2 = Museum.builder()
                .name("Museum B")
                .area("Busan")
                .id(2L)
                .build();

        List<String> names = Arrays.asList("Museum A", "Museum B");
        List<Museum> museumList = Arrays.asList(museum1, museum2);

        when(museumRepository.findByNameIn(names)).thenReturn(museumList);

        // When
        List<Museum> result = museumService.findMuseumsByName(names);

        // Then
        assertEquals(2, result.size());
        assertEquals("Museum A", result.get(0).getName());
        assertEquals("Museum B", result.get(1).getName());
    }
}
