package com.doma.artserver.api.munhwa;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.XMLParser;
import com.doma.artserver.util.storage.StorageService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MunwhaExhibitionApiClient implements ApiClient<MunwhaExhibitionDTO> {

    private final RestTemplate restTemplate;
    private final XMLParser<MunwhaExhibitionDTO> xmlParser;
    private final CloseableHttpClient httpClient;
    private final StorageService storageService;

    @Value("${spring.api.gonggongkey}")
    private String API_KEY;
    private static final String BASE_URL = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/period";

    MunwhaExhibitionApiClient(RestTemplate restTemplate,
                              XMLParser<MunwhaExhibitionDTO> xmlParser,
                              CloseableHttpClient httpClient,
                              StorageService<byte[]> storageService) {
        this.restTemplate = restTemplate;
        this.xmlParser = xmlParser;
        this.httpClient = httpClient;
        this.storageService = storageService;
    }

    @Override
    public List<MunwhaExhibitionDTO> fetchItems(int page) {
        URI url = generateUrl(page);
        String response = restTemplate.getForObject(url, String.class);

        String utf8Response;
        try {
            utf8Response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 변환 실패", e);
        }

        List<MunwhaExhibitionDTO> exhibitionList;
        try {
            exhibitionList = xmlParser.parse(utf8Response);
            System.out.println("List" + exhibitionList);
        } catch (Exception e) {
            throw new RuntimeException("파싱 실패", e);
        }

        // Stream API를 이용하여 'realmName'이 "미술"인 항목 필터링
        return exhibitionList.stream()
                .filter(exhibition -> "미술".equals(exhibition.getRealmName()))
                .peek(exhibition -> {
                    byte[] imageData = fetchImageData(exhibition.getThumbnail());
                    if (imageData != null) {
                        String storageUrl = storageService.uploadFile(exhibition.getTitle(), imageData);
                        exhibition.setStorageUrl(storageUrl);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public URI generateUrl(int page) {
        try {
            return new URI(BASE_URL + "?from=20240801&cPage=" + page +
                    "&rows=100&place=&gpsxfrom=&gpsyfrom=&gpsxto=&gpsyto=&keyword=&sortStdr=1&serviceKey=" + API_KEY);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL 생성 실패");
        }
    }

    public byte[] fetchImageData(String imageUrl) {
        HttpGet request = new HttpGet(imageUrl);
        request.setHeader("User-Agent", "Mozilla/5.0");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println(entity);
                return EntityUtils.toByteArray(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
