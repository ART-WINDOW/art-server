package com.doma.artserver.api.munhwa.exhibition;

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
        } catch (Exception e) {
            throw new RuntimeException("파싱 실패", e);
        }

        // Stream API를 이용하여 'realmName'이 "미술"인 항목 필터링
        return exhibitionList.stream()
                .filter(exhibition -> "미술".equals(exhibition.getRealmName()))
                .peek(exhibition -> {
                    byte[] imageData = null;
                    if (exhibition.getThumbnail() != null) {
                        imageData = fetchImageData(exhibition.getThumbnail());
                    }
                    if (imageData != null) {
                        // 이미지 URL에서 확장자 추출
                        String fileExtension = extractFileExtension(exhibition.getThumbnail());
                        // 확장자가 추가된 파일 이름으로 이미지 업로드
                        String storageUrl = storageService.uploadFile(exhibition.getTitle() + fileExtension, imageData);
                        exhibition.setStorageUrl(storageUrl);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public URI generateUrl(int page) {
        try {
            return new URI(BASE_URL + "?from=20240801&cPage=" + page +
                    "&rows=50&place=&gpsxfrom=&gpsyfrom=&gpsxto=&gpsyto=&keyword=&sortStdr=1&serviceKey=" + API_KEY);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL 생성 실패");
        }
    }

    @Override
    public List<MunwhaExhibitionDTO> fetchItems(Long apiId) {
        return List.of();
    }

    @Override
    public URI generateUrl(Long seq) {
        return null;
    }

    public byte[] fetchImageData(String imageUrl) {
        // imageUrl이 null이거나 빈 값인지 확인
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.err.println("Image URL is null or empty");
            return null;
        }

        HttpGet request = new HttpGet(imageUrl);
        request.setHeader("User-Agent", "Mozilla/5.0"); // User-Agent 설정

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity(); // 응답 엔티티 추출
            if (entity != null) {
                System.out.println(entity);
                return EntityUtils.toByteArray(entity); // 엔티티의 바이트 배열 반환
            } else {
                System.err.println("No entity found in the response");
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
        }

        return null; // 실패 시 null 반환
    }

    // URL에서 파일 확장자를 추출하는 메서드
    private String extractFileExtension(String imageUrl) {
        try {
            // URL에서 확장자 추출
            return imageUrl.substring(imageUrl.lastIndexOf("."));
        } catch (Exception e) {
            // 확장자 추출 실패 시 기본값을 반환 (예: .jpg)
            System.err.println("Failed to extract file extension from URL: " + imageUrl);
            return ".jpg"; // 기본 확장자
        }
    }
}
