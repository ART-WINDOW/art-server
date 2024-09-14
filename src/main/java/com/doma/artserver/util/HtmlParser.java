package com.doma.artserver.util;

import org.springframework.web.util.HtmlUtils;

public class HtmlParser {

    public static String parseHtmlEntities(String input) {
        return HtmlUtils.htmlUnescape(input);
    }
}
