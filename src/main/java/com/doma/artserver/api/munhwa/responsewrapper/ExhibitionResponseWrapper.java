package com.doma.artserver.api.munhwa.responsewrapper;

import com.doma.artserver.dto.exhibition.MunwhaPortalExhibitionDTO;
import com.doma.artserver.dto.museum.MunwhaPortalMuseumDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "response") // XML 루트 엘리먼트 이름을 명시
public class ExhibitionResponseWrapper {
    @XmlElement(name = "perforList") // XML에서 각 박물관 정보를 담고 있는 태그 이름
    private List<MunwhaPortalExhibitionDTO> exhibitions;

    public List<MunwhaPortalExhibitionDTO> getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(List<MunwhaPortalExhibitionDTO> exhibitions) {
        this.exhibitions = exhibitions;
    }
}

