package com.doma.artserver.service;

import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import com.doma.artserver.domain.majormuseum.repository.MajorMuseumRepository;
import com.doma.artserver.dto.majormuseum.MajorMuseumDTO;
import com.doma.artserver.service.majormuseum.MajorMuseumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MajorMuseumServiceImplTest {

    @Mock
    private MajorMuseumRepository majorMuseumRepository;

    @InjectMocks
    private MajorMuseumServiceImpl majorMuseumService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMajorMuseums() {
        // Given
        MajorMuseum majorMuseum1 = MajorMuseum.builder()
                .id(1L)
                .name("National Museum A")
                .area("Seoul")
                .website("http://museumA.com")
                .contactInfo("02-1234-5678")
                .museumId(101L)
                .build();

        MajorMuseum majorMuseum2 = MajorMuseum.builder()
                .id(2L)
                .name("National Museum B")
                .area("Busan")
                .website("http://museumB.com")
                .contactInfo("051-9876-5432")
                .museumId(102L)
                .build();

        when(majorMuseumRepository.findAll()).thenReturn(Arrays.asList(majorMuseum1, majorMuseum2));

        // When
        List<MajorMuseumDTO> result = majorMuseumService.getMajorMuseums();

        // Then
        assertEquals(2, result.size());
        assertEquals("National Museum A", result.get(0).getName());
        assertEquals("National Museum B", result.get(1).getName());
    }

    @Test
    public void testSaveMajorMuseum() {
        // Given
        MajorMuseum majorMuseum = MajorMuseum.builder()
                .id(1L)
                .name("National Museum A")
                .area("Seoul")
                .website("http://museumA.com")
                .contactInfo("02-1234-5678")
                .museumId(101L)
                .build();

        // When
        majorMuseumService.saveMajorMuseum(majorMuseum);

        // Then
        verify(majorMuseumRepository, times(1)).save(majorMuseum);
    }
}