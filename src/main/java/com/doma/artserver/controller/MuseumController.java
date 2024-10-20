package com.doma.artserver.controller;

import com.doma.artserver.dto.museum.MuseumDTO;
import com.doma.artserver.service.MuseumExhibitionFacade;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/museums")
public class MuseumController {

    private final MuseumExhibitionFacade museumExhibitionFacade;

    public MuseumController(MuseumExhibitionFacade museumExhibitionFacade) {
        this.museumExhibitionFacade = museumExhibitionFacade;
    }

    // 미술관 목록을 반환하는 GET 요청 처리
    @GetMapping
    public ResponseEntity<Page<MuseumDTO>> getMuseums(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int pageSize) {
        Page<MuseumDTO> museums = museumExhibitionFacade.getMuseums(page, pageSize);

        return ResponseEntity.ok(museums);
    }
}
