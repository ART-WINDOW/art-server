package com.doma.artserver.facade;

import com.doma.artserver.config.MajorMuseumConfig;
import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import com.doma.artserver.domain.museum.entity.Museum;
import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import com.doma.artserver.dto.majormuseum.MajorMuseumDTO;
import com.doma.artserver.dto.museum.MuseumDTO;
import com.doma.artserver.service.MuseumExhibitionFacadeImpl;
import com.doma.artserver.service.exhibition.ExhibitionService;
import com.doma.artserver.service.majormuseum.MajorMuseumService;
import com.doma.artserver.service.museum.MuseumService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MuseumExhibitionFacadeImplTest {

    @Mock
    private MuseumService museumService;

    @Mock
    private ExhibitionService exhibitionService;

    @Mock
    private MajorMuseumService majorMuseumService;

    @Mock
    private CloseableHttpClient httpClient;

    @InjectMocks
    private MuseumExhibitionFacadeImpl museumExhibitionFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadData() {
        // When
        museumExhibitionFacade.loadData();

        // Then
        verify(museumService, times(1)).fetchMuseum();
        verify(exhibitionService, times(1)).fetchExhibitions();
    }

    @Test
    public void testUpdateData() {
        // When
        museumExhibitionFacade.updateData();

        // Then
        verify(exhibitionService, times(1)).updateExhibitions();
    }

    @Test
    public void testFetchImageData() throws IOException {
        // Given
        String imageUrl = "http://example.com/image.jpg";
        byte[] expectedData = new byte[]{1, 2, 3};

        // Mocking HTTP client response
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        InputStream inputStream = new ByteArrayInputStream(expectedData);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        doReturn(inputStream).when(entity).getContent();

        // When
        byte[] actualData = museumExhibitionFacade.fetchImageData(imageUrl);

        // Then
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testSaveMajorMuseumsByNames() {
        // Given
        List<String> museumNames = Arrays.asList("Museum A", "Museum B");
        Museum museumA = Museum.builder().id(1L).name("Museum A").area("Seoul").build();
        Museum museumB = Museum.builder().id(2L).name("Museum B").area("Busan").build();
        when(museumService.findMuseumsByName(museumNames)).thenReturn(Arrays.asList(museumA, museumB));

        // When
        museumExhibitionFacade.saveMajorMuseumsByNames(museumNames);

        // Then
        verify(majorMuseumService, times(2)).saveMajorMuseum(any(MajorMuseum.class));
    }

    @Test
    public void testGetExhibitions() {
        // Given
        ExhibitionDTO exhibition1 = ExhibitionDTO.builder().id(1L).title("Exhibition A").imgUrl("http://example.com/image1.jpg").build();
        ExhibitionDTO exhibition2 = ExhibitionDTO.builder().id(2L).title("Exhibition B").imgUrl("http://example.com/image2.jpg").build();
        Page<ExhibitionDTO> exhibitionPage = new PageImpl<>(Arrays.asList(exhibition1, exhibition2));
        when(exhibitionService.getExhibitions(0, 10)).thenReturn(exhibitionPage);

        // Mocking HTTP client response for image fetching
        try {
            CloseableHttpResponse response = mock(CloseableHttpResponse.class);
            HttpEntity entity = mock(HttpEntity.class);
            InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
            when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
            when(response.getEntity()).thenReturn(entity);
            doReturn(inputStream).when(entity).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // When
        Page<ExhibitionDTO> result = museumExhibitionFacade.getExhibitions(0, 10);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals("Exhibition A", result.getContent().get(0).getTitle());
        assertEquals("Exhibition B", result.getContent().get(1).getTitle());
    }

    @Test
    public void testGetMajorExhibitions() {
        // Given
        MajorMuseumDTO majorMuseum1 = MajorMuseumDTO.builder().museumId(1L).build();
        MajorMuseumDTO majorMuseum2 = MajorMuseumDTO.builder().museumId(2L).build();
        when(majorMuseumService.getMajorMuseums()).thenReturn(Arrays.asList(majorMuseum1, majorMuseum2));

        ExhibitionDTO exhibition1 = ExhibitionDTO.builder().id(1L).title("Exhibition A").imgUrl("http://example.com/image1.jpg").build();
        ExhibitionDTO exhibition2 = ExhibitionDTO.builder().id(2L).title("Exhibition B").imgUrl("http://example.com/image2.jpg").build();
        Page<ExhibitionDTO> exhibitionPage = new PageImpl<>(Arrays.asList(exhibition1, exhibition2));
        when(exhibitionService.getExhibitionsByMuseums(anyList(), eq(0), eq(10))).thenReturn(exhibitionPage);

        // Mocking HTTP client response for image fetching
        try {
            CloseableHttpResponse response = mock(CloseableHttpResponse.class);
            HttpEntity entity = mock(HttpEntity.class);
            InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
            when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
            when(response.getEntity()).thenReturn(entity);
            doReturn(inputStream).when(entity).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // When
        Page<ExhibitionDTO> result = museumExhibitionFacade.getMajorExhibitions(0, 10);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals("Exhibition A", result.getContent().get(0).getTitle());
        assertEquals("Exhibition B", result.getContent().get(1).getTitle());
    }
}