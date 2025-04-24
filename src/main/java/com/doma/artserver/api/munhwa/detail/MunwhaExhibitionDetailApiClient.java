package com.doma.artserver.api.munhwa.detail;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.XMLParser;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class MunwhaExhibitionDetailApiClient implements ApiClient<MunwhaExhibitionDetailDTO> {

    private final RestTemplate restTemplate;
    private final XMLParser<MunwhaExhibitionDetailDTO> xmlParser;
    private final CloseableHttpClient httpClient;

    MunwhaExhibitionDetailApiClient(RestTemplate restTemplate,
                                    XMLParser<MunwhaExhibitionDetailDTO> xmlParser,
                                    CloseableHttpClient httpClient) {
        this.restTemplate = restTemplate;
        this.xmlParser = xmlParser;
        this.httpClient = httpClient;
    }

    @Value("${spring.api.gonggongkey}")
    private String API_KEY;
    private static final String BASE_URL = "https://apis.data.go.kr/B553457/nopenapi/rest/publicperformancedisplays/detail";

    @Override
    public List<MunwhaExhibitionDetailDTO> fetchItems(int page) {
        return List.of();
    }

    @Override
    public URI generateUrl(int page) {
        return null;
    }

    @Override
    public List<MunwhaExhibitionDetailDTO> fetchItems(Long apiId) {
        URI url = generateUrl(apiId);
        String response = restTemplate.getForObject(url, String.class);

        String utf8Response;
        try {
            utf8Response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 변환 실패", e);
        }

        List<MunwhaExhibitionDetailDTO> detailList;

        try {
            detailList = xmlParser.parse(utf8Response);
        } catch (Exception e) {
            throw new RuntimeException("파싱 실패", e);
        }

        return detailList;
    }

    @Override
    public URI generateUrl(Long apiId) {
        try {
            return new URI(BASE_URL + "?serviceKey=" + API_KEY + "&seq=" + apiId);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL 생성 실패");
        }
    }
}
