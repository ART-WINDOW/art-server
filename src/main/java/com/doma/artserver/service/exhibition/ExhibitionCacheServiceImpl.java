package com.doma.artserver.service.exhibition;

import com.doma.artserver.dto.exhibition.ExhibitionDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<ExhibitionDTO> getExhibitions(int page, int pageSize) {
        return List.of();
    }


}
