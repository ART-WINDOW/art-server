package com.doma.artserver.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HtmlParserTest {

    @Test
    public void testParseHtml() {
        // Given
        String input = "&lt;p&gt;Hello &amp; welcome!&lt;/p&gt;";
        String expectedOutput = "<p>Hello & welcome!</p>";

        // When
        String actualOutput = HtmlParser.parseHtml(input);

        // Then
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseHtmlWithNoEscapeCharacters() {
        // Given
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";

        // When
        String actualOutput = HtmlParser.parseHtml(input);

        // Then
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseHtmlWithMultipleEscapeCharacters() {
        // Given
        String input = "&lt;div&gt;This &amp; that&lt;/div&gt;";
        String expectedOutput = "<div>This & that</div>";

        // When
        String actualOutput = HtmlParser.parseHtml(input);

        // Then
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseHtmlWithNullInput() {
        // Given
        String input = null;

        // When
        String actualOutput = HtmlParser.parseHtml(input);

        // Then
        assertNull(actualOutput);
    }

    @Test
    public void testParseHtmlWithEmptyString() {
        // Given
        String input = "";
        String expectedOutput = "";

        // When
        String actualOutput = HtmlParser.parseHtml(input);

        // Then
        assertEquals(expectedOutput, actualOutput);
    }

}