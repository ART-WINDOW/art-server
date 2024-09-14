package com.doma.artserver.api.munhwa;

import com.doma.artserver.api.ApiClient;
import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
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
import java.util.ArrayList;
import java.util.List;

@Component
public class MunwhaPortalApiClient implements ApiClient {

    private final RestTemplate restTemplate;

    @Value("${spring.api.gonggongkey}")  // application.yml에서 값을 가져옴
    private String API_KEY;
    private static final String BASE_URL = "http://www.culture.go.kr/openapi/rest/publicperformancedisplays/period";

    public MunwhaPortalApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 전체 페이지 수를 반환하는 메서드
    @Override
    public int getTotalPages() {
        String url = generateUrl(1);  // 첫 번째 페이지 호출
        String response = restTemplate.getForObject(url, String.class);

        // XML에서 totalCount 값을 파싱하여 페이지 수 계산
        int totalCount = parseTotalCount(response);
        int rowsPerPage = 10;  // 한 페이지에 몇 개의 row를 가져오는지

        return (int) Math.ceil((double) totalCount / rowsPerPage);
    }

    private int parseTotalCount(String xmlResponse) {
        try {
            // DOM Parser 설정
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlResponse));
            Document document = builder.parse(is);

            // <totalCount> 태그를 찾고 값을 가져옴
            NodeList totalCountNode = document.getElementsByTagName("totalCount");
            String totalCountStr = totalCountNode.item(0).getTextContent();

            return Integer.parseInt(totalCountStr);

        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 오류 발생", e);
        }
    }

    // 특정 페이지에 해당하는 URL을 동적으로 생성
    @Override
    public String generateUrl(int page) {
        return BASE_URL + "?from=20240801&cPage=" + page +
                "&rows=10&place=&gpsxfrom=&gpsyfrom=&gpsxto=&gpsyto=&keyword=&sortStdr=1&serviceKey=" + API_KEY;
    }

    // 특정 페이지에서 데이터를 가져오는 메서드
    @Override
    public List<MunwhaPortalExhibitionDTO> fetchExhibitions(int page) {
        String url = generateUrl(page);
        String response = restTemplate.getForObject(url, String.class);

        // XML 파싱 로직 추가 (JAXB 또는 다른 라이브러리 이용)
        // ExhibitionDTO 객체로 변환 후 리스트 반환
        return parseExhibitions(response);
    }

    private List<MunwhaPortalExhibitionDTO> parseExhibitions(String xmlResponse) {
        List<MunwhaPortalExhibitionDTO> exhibitions = new ArrayList<>();

        try {
            // JAXBContext 생성
            JAXBContext jaxbContext = JAXBContext.newInstance(MunwhaPortalExhibitionDTO.class);

            // Unmarshaller 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // XML을 DTO로 변환
            StringReader reader = new StringReader(xmlResponse);
            MunwhaPortalExhibitionDTO exhibitionDTO = (MunwhaPortalExhibitionDTO) unmarshaller.unmarshal(reader);

            exhibitions.add(exhibitionDTO); // 변환된 DTO 리스트에 추가
        } catch (JAXBException e) {
            e.printStackTrace(); // 예외 처리
        }

        return exhibitions;
    }

}
