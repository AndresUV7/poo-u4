import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import models.Streaming;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class StreamingTest {
    private Streaming streaming;
    private Set<String> platforms;

    @BeforeEach
    void setUp() {
        // Create platforms
        platforms = new HashSet<>();
        platforms.add("Netflix");
        platforms.add("Amazon Prime");

        // Create streaming
        streaming = Streaming.builder()
                .title("Tech Conference Highlights")
                .minutesDuration(120)
                .genre("Technology")
                .platforms(platforms)
                .isLive(true)
                .build();
    }

    @Test
    @DisplayName("Streaming Builder Creates Instance Correctly")
    void testStreamingBuilder() {
        assertNotNull(streaming);
        assertEquals("Tech Conference Highlights", streaming.getTitle());
        assertEquals(120, streaming.getMinutesDuration());
        assertEquals("Technology", streaming.getGenre());
        assertEquals(platforms, streaming.getPlatforms());
        assertEquals(streaming.getIsLive(), "Si");
    }

    @Test
    @DisplayName("Show Details Prints Correct Information")
    void testShowDetails() {
        // Capture system output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Call show details
        streaming.showDetails();

        String output = outContent.toString();
        System.setOut(System.out);

        // Verify output
        assertTrue(output.contains("Título: Tech Conference Highlights"), "Title should be present");
        assertTrue(output.contains("Duración en minutos: 120"), "Duration should be present");
        assertTrue(output.contains("Género: Technology"), "Genre should be present");
        assertTrue(output.contains("Plataformas: Netflix, Amazon Prime"), "Platforms should be present");
        assertTrue(output.contains("En vivo: Si"), "Live status should be present");
        assertTrue(Pattern.compile("ID: [A-Z]{2}\\d{3}-\\d").matcher(output).find(), 
            "ID should be present and follow the format");
    }

    @Test
    @DisplayName("Unimplemented Methods Throw UnsupportedOperationException")
    void testUnimplementedMethods() {
        assertThrows(UnsupportedOperationException.class, () -> streaming.getName());
        assertThrows(UnsupportedOperationException.class, () -> streaming.getContact());
        assertThrows(UnsupportedOperationException.class, () -> streaming.getLinkedInUser());
    }

    @Test
    @DisplayName("Platforms Can Be Modified")
    void testPlatformsModification() {
        streaming.getPlatforms().add("Hulu");

        assertTrue(streaming.getPlatforms().contains("Hulu"));
        assertEquals(3, streaming.getPlatforms().size());
    }
}