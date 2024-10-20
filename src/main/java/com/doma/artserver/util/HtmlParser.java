package com.doma.artserver.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class HtmlParser {
    public static String parseHtml(String input) {
        if (input == null) {
            return null; // null 입력 처리
        }
        return HtmlUtils.htmlUnescape(input);
    }
}
