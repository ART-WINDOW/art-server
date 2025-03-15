package com.doma.artserver.service.majormuseum;

import com.doma.artserver.domain.majormuseum.entity.MajorMuseum;
import com.doma.artserver.domain.majormuseum.repository.MajorMuseumRepository;
import com.doma.artserver.dto.majormuseum.MajorMuseumDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MajorMuseumServiceImpl implements MajorMuseumService{

    private static final Logger logger = LoggerFactory.getLogger(MajorMuseumServiceImpl.class);
    private final MajorMuseumRepository majorMuseumRepository;

    public MajorMuseumServiceImpl(MajorMuseumRepository majorMuseumRepository) {
        this.majorMuseumRepository = majorMuseumRepository;
    }

    @Override
    @Cacheable(value = "majorMuseums")
    public List<MajorMuseumDTO> getMajorMuseums() {
        logger.info("Fetching major museums from database");
        return majorMuseumRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "majorMuseums", allEntries = true)
    public void saveMajorMuseum(MajorMuseum majorMuseum) {
        logger.info("Saving major museum and evicting cache: {}", majorMuseum.getName());
        Optional<MajorMuseum> existingMajorMuseum = majorMuseumRepository.findByMuseumId(majorMuseum.getMuseumId());
        if (existingMajorMuseum.isEmpty()) {
            majorMuseumRepository.save(majorMuseum);
        }
    }

    @Override
    @CacheEvict(value = "majorMuseums", allEntries = true)
    public void deleteMajorMuseum(Long id) {
        logger.info("Deleting major museum and evicting cache: {}", id);
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
