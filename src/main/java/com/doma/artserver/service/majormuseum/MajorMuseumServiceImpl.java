package com.doma.artserver.service.majormuseum;

import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import com.doma.artserver.domain.majormuseum.repository.MajorMuseumRepository;
import com.doma.artserver.dto.majormuseum.MajorMuseumDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Override
    public void saveMajorMuseum(MajorMuseum majorMuseum) {
        Optional<MajorMuseum> existingMajorMuseum = majorMuseumRepository.findByName(majorMuseum.getName());
        if (existingMajorMuseum.isPresent()) {
            return;
        }
        majorMuseumRepository.save(majorMuseum);
    }

    @Override
    public void deleteMajorMuseum(Long id) {
        majorMuseumRepository.deleteById(id);
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
