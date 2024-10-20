package com.doma.artserver.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateAdapterTest {

    private final LocalDateAdapter adapter = new LocalDateAdapter();

    @Test
    public void testUnmarshal() throws Exception {
        // Given
        String dateStr = "20231011";
        LocalDate expectedDate = LocalDate.of(2023, 10, 11);

        // When
        LocalDate actualDate = adapter.unmarshal(dateStr);

        // Then
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testMarshal() throws Exception {
        // Given
        LocalDate date = LocalDate.of(2023, 10, 11);
        String expectedDateStr = "20231011";

        // When
        String actualDateStr = adapter.marshal(date);

        // Then
        assertEquals(expectedDateStr, actualDateStr);
    }
}