package com.doma.artserver.controller;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import com.doma.artserver.service.MuseumExhibitionFacade;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exhibitions")
public class ExhibitionController {

    private final MuseumExhibitionFacade museumExhibitionFacade;

    public ExhibitionController(@Qualifier("museumExhibitionFacadeImpl") MuseumExhibitionFacade museumExhibitionFacade) {
        this.museumExhibitionFacade = museumExhibitionFacade;
    }

    // 전시 목록을 반환하는 GET 요청 처리
    @GetMapping
    public ResponseEntity<Page<ExhibitionDTO>> getExhibitions(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int pageSize) {
        Page<ExhibitionDTO> exhibitions = museumExhibitionFacade.getExhibitions(page, pageSize);

        return ResponseEntity.ok(exhibitions);
    }

    @GetMapping("/major")
    public ResponseEntity<Page<ExhibitionDTO>> getMajorExhibitions(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "20") int pageSize) {
        Page<ExhibitionDTO> exhibitions = museumExhibitionFacade.getMajorExhibitions(page, pageSize);
        exhibitions.forEach(exhibition -> System.out.println("Exhibition Title: " + exhibition.getTitle()));

        return ResponseEntity.ok(exhibitions);
    }

    @GetMapping("/area")
    public ResponseEntity<Page<ExhibitionDTO>> getExhibitionsByArea(@RequestParam String area,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int pageSize) {
        Page<ExhibitionDTO> exhibitions = museumExhibitionFacade.getExhibitionsByArea(area, page, pageSize);

        return ResponseEntity.ok(exhibitions);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ExhibitionDTO>> searchExhibitions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String area,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Page<ExhibitionDTO> results = museumExhibitionFacade.searchExhibitions(
                keyword, area, page, pageSize);
        return ResponseEntity.ok(results);
    }






}
