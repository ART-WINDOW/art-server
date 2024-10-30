package com.doma.artserver.api.munhwa.detail;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Getter
@Setter
@XmlRootElement(name = "perforInfo")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MunwhaExhibitionDetailDTO {

    @XmlElement(name = "price")
    private String price;

    @XmlElement(name = "url")
    private String url;

}
