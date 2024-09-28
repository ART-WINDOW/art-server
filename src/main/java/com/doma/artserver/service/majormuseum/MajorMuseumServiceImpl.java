package com.doma.artserver.service.majormuseum;

import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import com.doma.artserver.domain.majormuseum.repository.MajorMuseumRepository;
import com.doma.artserver.domain.museum.repository.MuseumRepository;
import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import com.doma.artserver.dto.majormuseum.MajorMuseumDTO;
import com.doma.artserver.dto.museum.MuseumDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MajorMuseumServiceImpl implements MajorMuseumService{

    private final MajorMuseumRepository majorMuseumRepository;

    public MajorMuseumServiceImpl(MajorMuseumRepository majorMuseumRepository) {
        this.majorMuseumRepository = majorMuseumRepository;
    }

    @Override
    public List<MajorMuseumDTO> getMajorMuseums() {
        return majorMuseumRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // MajorMuseum을 MuseumDTO로 변환하는 메서드
    private MajorMuseumDTO convertToDto(MajorMuseum majorMuseum) {
        return MajorMuseumDTO.builder()
                .id(majorMuseum.getId())
                .name(majorMuseum.getName())
                .area(majorMuseum.getArea())
                .gpsX(majorMuseum.getGpsX())
                .gpsY(majorMuseum.getGpsY())
                .website(majorMuseum.getWebsite())
                .contactInfo(majorMuseum.getContactInfo())
                .museumId(majorMuseum.getMuseumId())
                .build();
    }
}
