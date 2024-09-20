package com.doma.artserver.api.munhwa;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class MunwhaPortalApiClient implements ApiClient {

    private final RestTemplate restTemplate;

    @Value("${spring.api.gonggongkey}")
    private String API_KEY;
    private static final String BASE_URL = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/period";

    public MunwhaPortalApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    // 특정 페이지에 해당하는 URL을 동적으로 생성
    @Override
    public URI generateUrl(int page) {
        try {
            return new URI(BASE_URL + "?from=20240801&cPage=" + page +
                    "&rows=10&place=&gpsxfrom=&gpsyfrom=&gpsxto=&gpsyto=&keyword=&sortStdr=1&serviceKey=" + API_KEY);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL 생성 실패");
        }
    }

    @Override
    public List<MunwhaPortalExhibitionDTO> fetchExhibitions(int page) {
        return List.of();
    }

    @Override
    public List<MunwhaPortalMuseumDTO> fetchMuseums(int page) {
        return List.of();
    }






}
