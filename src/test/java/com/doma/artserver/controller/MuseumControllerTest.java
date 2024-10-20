package com.doma.artserver.controller;

import com.doma.artserver.dto.museum.MuseumDTO;
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
public class MuseumControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private MuseumExhibitionFacade museumExhibitionFacade;

    @InjectMocks
    private MuseumController museumController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetMuseums() throws Exception {
        // Given
        MuseumDTO museum1 = MuseumDTO.builder().id(1L).name("Museum A").build();
        MuseumDTO museum2 = MuseumDTO.builder().id(2L).name("Museum B").build();
        Page<MuseumDTO> museumPage = new PageImpl<>(Arrays.asList(museum1, museum2));
        when(museumExhibitionFacade.getMuseums(0, 20)).thenReturn(museumPage);

        // When & Then
        mockMvc.perform(get("/api/v1/museums")
                        .param("page", "0")
                        .param("pageSize", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}