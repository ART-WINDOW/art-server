package com.doma.artserver.util;

import org.springframework.web.util.HtmlUtils;

public class HtmlParser {
    public static String parseHtml(String input) {
        return HtmlUtils.htmlUnescape(input);
    }
}
