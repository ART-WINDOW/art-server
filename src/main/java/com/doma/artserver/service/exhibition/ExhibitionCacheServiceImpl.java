package com.doma.artserver.service.exhibition;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExhibitionCacheServiceImpl implements ExhibitionCacheService {

    private final RedisTemplate<String, ExhibitionDTO> redisTemplate;

    public ExhibitionCacheServiceImpl(RedisTemplate<String, ExhibitionDTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveExhibition(ExhibitionDTO exhibitionDTO) {
        String key = "exhibition:" + exhibitionDTO.getId();
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, exhibitionDTO); // 타임아웃 없이 저장
        }
    }

    @Override
    public ExhibitionDTO getExhibition(Long id) {
        return redisTemplate.opsForValue().get("exhibition:" + id);
    }

    @Override
    public void deleteExhibition(Long id) {
        redisTemplate.delete("exhibition:" + id);
    }

    @Override
    public void updateExhibition(ExhibitionDTO exhibitionDTO) {
        redisTemplate.opsForValue().set("exhibition:" + exhibitionDTO.getId(), exhibitionDTO);
    }

    @Override
    public void clearCache() {
        redisTemplate.delete(redisTemplate.keys("exhibition:*"));
    }

    @Override
    public Page<ExhibitionDTO> getExhibitions(int page, int pageSize) {
        // Redis에서 모든 전시회 키를 가져옴
        Set<String> keys = redisTemplate.keys("exhibition:*");
        Pageable pageable = PageRequest.of(page, pageSize);

        // 키를 정렬하고 페이지네이션을 적용
        List<String> sortedKeys = new ArrayList<>(keys);
        Collections.sort(sortedKeys);

        int start = page * pageSize;
        int end = Math.min(start + pageSize, sortedKeys.size());

        // 유효성 검사
        if (start > end) {
            return new PageImpl<>(Collections.emptyList(), pageable, sortedKeys.size());
        }

        // 페이지 범위 내의 키를 가져옴
        List<String> pageKeys = sortedKeys.subList(start, end);

        // 해당 키들에 대한 전시회 데이터를 가져옴
        List<ExhibitionDTO> exhibitions = redisTemplate.opsForValue().multiGet(pageKeys);

        // null 값을 제거하고 Page 객체로 반환
        List<ExhibitionDTO> filteredExhibitions = exhibitions.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(filteredExhibitions, pageable, sortedKeys.size());
    }


}
