package com.doma.artserver.service.exhibition;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExhibitionCacheServiceImpl implements ExhibitionCacheService {

    private final RedisTemplate<String, ExhibitionDTO> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public ExhibitionCacheServiceImpl(
            RedisTemplate<String, ExhibitionDTO> redisTemplate,
            StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void saveExhibition(ExhibitionDTO exhibitionDTO) {
        String exhibitionKey = "exhibition:" + exhibitionDTO.getId();
        String areaKey = "area:" + exhibitionDTO.getArea();

        // 전시 정보 저장
        redisTemplate.opsForValue().set(exhibitionKey, exhibitionDTO);

        // 지역별 Sorted Set에 전시 ID 추가
        double score = exhibitionDTO.getStartDate().atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        stringRedisTemplate.opsForZSet().add(areaKey, exhibitionDTO.getId().toString(), score);
    }

    @Override
    public ExhibitionDTO getExhibition(Long id) {
        return redisTemplate.opsForValue().get("exhibition:" + id);
    }

    @Override
    public void deleteExhibition(Long id) {
        ExhibitionDTO exhibition = getExhibition(id);
        if (exhibition != null) {
            String exhibitionKey = "exhibition:" + id;
            String areaKey = "area:" + exhibition.getArea();

            redisTemplate.delete(exhibitionKey);
            stringRedisTemplate.opsForZSet().remove(areaKey, id.toString());
        }
    }

    @Override
    public void updateExhibition(ExhibitionDTO exhibitionDTO) {
        saveExhibition(exhibitionDTO); // 저장 로직을 재사용
    }

    @Override
    public Page<ExhibitionDTO> getExhibitionsByArea(String area, int page, int pageSize) {
        String areaKey = "area:" + area;

        // 해당 지역의 전체 전시 수 조회
        Long total = stringRedisTemplate.opsForZSet().size(areaKey);
        if (total == null || total == 0) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, pageSize), 0);
        }

        // ZSet에서 해당 페이지의 exhibition ID들을 가져옴 (시작일 기준 내림차순 정렬)
        int start = page * pageSize;
        int end = start + pageSize - 1;
        Set<String> exhibitionIds = stringRedisTemplate.opsForZSet().reverseRange(areaKey, start, end);

        if (exhibitionIds == null || exhibitionIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, pageSize), total);
        }

        // 각 ID에 해당하는 Exhibition 상세 정보를 한 번에 조회
        List<String> exhibitionKeys = exhibitionIds.stream()
                .map(id -> "exhibition:" + id)
                .collect(Collectors.toList());

        List<ExhibitionDTO> exhibitions = Optional.ofNullable(redisTemplate.opsForValue().multiGet(exhibitionKeys))
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(exhibitions, PageRequest.of(page, pageSize), total);
    }

    @Override
    public Page<ExhibitionDTO> getExhibitions(int page, int pageSize) {
        // Redis에서 모든 전시회 키를 가져옴
        Set<String> keys = redisTemplate.keys("exhibition:*");
        if (keys == null || keys.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, pageSize), 0);
        }

        Pageable pageable = PageRequest.of(page, pageSize);

        // 키를 정렬하고 페이지네이션을 적용
        List<String> sortedKeys = new ArrayList<>(keys);
        Collections.sort(sortedKeys);

        int start = page * pageSize;
        int end = Math.min(start + pageSize, sortedKeys.size());

        // 유효성 검사
        if (start >= sortedKeys.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, sortedKeys.size());
        }

        // 페이지 범위 내의 키들에 대한 전시회 데이터를 가져옴
        List<ExhibitionDTO> exhibitions = redisTemplate.opsForValue()
                .multiGet(sortedKeys.subList(start, end)).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(exhibitions, pageable, sortedKeys.size());
    }

    @Override
    public void clearCache() {
        Set<String> exhibitionKeys = redisTemplate.keys("exhibition:*");
        Set<String> areaKeys = redisTemplate.keys("area:*");

        if (exhibitionKeys != null && !exhibitionKeys.isEmpty()) {
            redisTemplate.delete(exhibitionKeys);
        }
        if (areaKeys != null && !areaKeys.isEmpty()) {
            redisTemplate.delete(areaKeys);
        }
    }
}
