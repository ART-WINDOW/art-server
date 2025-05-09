package com.doma.artserver.api.munhwa.museum;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.XMLParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MunwhaMuseumApiClient implements ApiClient<MunwhaMuseumDTO> {

    private final RestTemplate restTemplate;
    private final XMLParser<MunwhaMuseumDTO> xmlParser;

    @Value("${spring.api.gonggongkey}")
    private String API_KEY;
    private static final String BASE_URL = "https://apis.data.go.kr/B553457/nopenapi/rest/publicperformancedisplays/period";

    public MunwhaMuseumApiClient(RestTemplate restTemplate,
                                 XMLParser<MunwhaMuseumDTO> xmlParser) {
        this.restTemplate = restTemplate;
        this.xmlParser = xmlParser;
    }


    @Override
    public List<MunwhaMuseumDTO> fetchItems(int page) {
        URI url = generateUrl(page);
        String response = restTemplate.getForObject(url, String.class);

        String utf8Response;
        try {
            utf8Response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 변환 실패", e);
        }

        List<MunwhaMuseumDTO> museumList;
        try {
            museumList = xmlParser.parse(utf8Response);
            System.out.println("List" + museumList);
        } catch (Exception e) {
            throw new RuntimeException("파싱 실패", e);
        }

        // Stream API를 이용하여 'realmName'이 "전시"인 항목 필터링
        return museumList.stream()
                .filter(museum -> "전시".equals(museum.getRealmName()))
                .collect(Collectors.toList());
    }

    // 특정 페이지에 해당하는 URL을 동적으로 생성
    @Override
    public URI generateUrl(int page) {
        try {
            return new URI(BASE_URL + "?from=20240801&cPage=" + page +
                    "&rows=100&place=&gpsxfrom=&gpsyfrom=&gpsxto=&gpsyto=&keyword=&sortStdr=1&serviceKey=" + API_KEY);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL 생성 실패");
        }
    }

    @Override
    public List<MunwhaMuseumDTO> fetchItems(Long apiId) {
        return List.of();
    }

    @Override
    public URI generateUrl(Long seq) {
        return null;
    }


}
