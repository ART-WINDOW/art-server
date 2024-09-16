package com.doma.artserver.api.munhwa;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.api.munhwa.responsewrapper.ExhibitionResponseWrapper;
import com.doma.artserver.api.munhwa.responsewrapper.MuseumResponseWrapper;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    // 특정 페이지에서 데이터를 가져오는 메서드
    @Override
    public List<MunwhaPortalExhibitionDTO> fetchExhibitions(int page) {
        URI url = generateUrl(page);
        String response = restTemplate.getForObject(url, String.class);

        // XML 파싱 로직 추가 (JAXB 또는 다른 라이브러리 이용)
        // ExhibitionDTO 객체로 변환 후 리스트 반환
        return parseExhibitions(response);
    }

    @Override
    public List<MunwhaPortalMuseumDTO> fetchMuseums(int page) {
        URI url = generateUrl(page);

        // RestTemplate에서 받은 응답을 UTF-8로 변환하는 부분 추가
        byte[] responseBytes = restTemplate.getForObject(url, byte[].class); // 바이트 배열로 응답 받기
        String response = new String(responseBytes, StandardCharsets.UTF_8);  // UTF-8로 변환

        return parseMuseums(response);
    }

    private List<MunwhaPortalExhibitionDTO> parseExhibitions(String xmlResponse) {
        List<MunwhaPortalExhibitionDTO> exhibitions = new ArrayList<>();

        try {
            // JAXBContext 생성
            JAXBContext jaxbContext = JAXBContext.newInstance(ExhibitionResponseWrapper.class);

            // Unmarshaller 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // XML을 DTO로 변환
            StringReader reader = new StringReader(xmlResponse);
            ExhibitionResponseWrapper exhibitionResponse = (ExhibitionResponseWrapper) unmarshaller.unmarshal(reader);

            exhibitions = exhibitionResponse.getExhibitions(); // 변환된 DTO 리스트에 추가
        } catch (JAXBException e) {
            e.printStackTrace(); // 예외 처리
        }

        return exhibitions;
    }

    private List<MunwhaPortalMuseumDTO> parseMuseums(String xmlResponse) {
        List<MunwhaPortalMuseumDTO> museums = new ArrayList<>();

        try {
            // JAXBContext 생성
            JAXBContext jaxbContext = JAXBContext.newInstance(MuseumResponseWrapper.class);

            // Unmarshaller 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // XML을 DTO로 변환
            StringReader reader = new StringReader(xmlResponse);
            MuseumResponseWrapper museumResponse = (MuseumResponseWrapper) unmarshaller.unmarshal(reader);

            // 변환된 DTO 리스트 반환
            museums = museumResponse.getMuseums();
        } catch (JAXBException e) {
            e.printStackTrace(); // 예외 처리
        }

        return museums;
    }


}
