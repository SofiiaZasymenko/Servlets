package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    public void testParseQueryString() {
        assertEquals(Map.of(), Utils.parseQueryString(null));
        assertEquals(Map.of(), Utils.parseQueryString("     "));
        assertEquals(Map.of("timezone", List.of("UTC+2")), Utils.parseQueryString("?timezone=UTC+2"));
        assertEquals(Map.of("timezone", List.of("UTC+2")), Utils.parseQueryString("     ?timezone=UTC+2"));
        assertEquals(Map.of("timezone", List.of("UTC+2")), Utils.parseQueryString("     timezone=UTC+2"));
        assertEquals(Map.of("timezone", List.of("UTC+2", "UTC+3")), Utils.parseQueryString("timezone=UTC+2,UTC+3"));
        assertEquals(Map.of("timezone", List.of("UTC+2", "UTC-2")), Utils.parseQueryString("timezone=UTC+2&timezone=UTC-2"));
        assertEquals(Map.of("timezone", List.of("UTC+2", "UTC-2", "UTC+3")), Utils.parseQueryString("timezone=UTC+2&timezone=UTC-2,UTC+3&"));
        assertEquals(Map.of("timezone", List.of("UTC+2", "UTC+3", "UTC-2"), "test", List.of("test")),
                Utils.parseQueryString("timezone=UTC+2,UTC+3&timezone=UTC-2&test=test&"));
        assertEquals(Map.of("timezone", List.of("UTC+2")),
                Utils.parseQueryString("timezone=UTC+2&timezone=&timezone=,&"));
    }

    @Test
    public void testIsTimezoneValid() {
        assertTrue(Utils.isTimezoneValid("UTC"));
        assertTrue(Utils.isTimezoneValid("UTC+2"));
        assertTrue(Utils.isTimezoneValid("UTC-2"));
        assertTrue(Utils.isTimezoneValid("GMT+2"));
        assertTrue(Utils.isTimezoneValid("GMT-2"));
        assertTrue(Utils.isTimezoneValid("Europe/Kyiv"));
        assertFalse(Utils.isTimezoneValid("test"));
    }
}