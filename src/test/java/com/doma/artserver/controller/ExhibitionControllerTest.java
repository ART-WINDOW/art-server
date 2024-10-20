package com.doma.artserver.controller;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import com.doma.artserver.service.MuseumExhibitionFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class ExhibitionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private MuseumExhibitionFacade museumExhibitionFacade;

    @InjectMocks
    private ExhibitionController exhibitionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetExhibitions() throws Exception {
        // Given
        ExhibitionDTO exhibition1 = ExhibitionDTO.builder().id(1L).title("Exhibition A").build();
        ExhibitionDTO exhibition2 = ExhibitionDTO.builder().id(2L).title("Exhibition B").build();
        Page<ExhibitionDTO> exhibitionPage = new PageImpl<>(Arrays.asList(exhibition1, exhibition2));
        when(museumExhibitionFacade.getExhibitions(0, 20)).thenReturn(exhibitionPage);

        // When & Then
        mockMvc.perform(get("/api/v1/exhibitions")
                        .param("page", "0")
                        .param("pageSize", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetMajorExhibitions() throws Exception {
        // Given
        ExhibitionDTO exhibition1 = ExhibitionDTO.builder().id(1L).title("Major Exhibition A").build();
        ExhibitionDTO exhibition2 = ExhibitionDTO.builder().id(2L).title("Major Exhibition B").build();
        Page<ExhibitionDTO> exhibitionPage = new PageImpl<>(Arrays.asList(exhibition1, exhibition2));
        when(museumExhibitionFacade.getMajorExhibitions(0, 20)).thenReturn(exhibitionPage);

        // When & Then
        mockMvc.perform(get("/api/v1/exhibitions/major")
                        .param("page", "0")
                        .param("pageSize", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testTestEndpoint() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/exhibitions/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("TEST"));
    }
}