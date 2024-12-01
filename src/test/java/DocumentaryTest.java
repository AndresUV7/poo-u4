import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import models.Documentary;
import models.Researcher;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentaryTest {
    private Documentary documentary;
    private Set<Researcher> researchers;

    @BeforeEach
    void setUp() {
        // Create mock researchers
        researchers = new HashSet<>();
        Researcher researcher1 = mock(Researcher.class);
        Researcher researcher2 = mock(Researcher.class);
        researchers.add(researcher1);
        researchers.add(researcher2);

        // Create documentary
        documentary = Documentary.builder()
                .title("Nature Exploration")
                .minutesDuration(60)
                .genre("Nature")
                .topic("Marine Ecosystems")
                .researchers(researchers)
                .build();
    }

    @Test
    @DisplayName("Documentary Builder Creates Instance Correctly")
    void testDocumentaryBuilder() {
        assertNotNull(documentary);
        assertEquals("Nature Exploration", documentary.getTitle());
        assertEquals(60, documentary.getMinutesDuration());
        assertEquals("Nature", documentary.getGenre());
        assertEquals("Marine Ecosystems", documentary.getTopic());
        assertEquals(researchers, documentary.getResearchers());
    }

    @Test
    @DisplayName("Show Details Prints Correct Information")
    void testShowDetails() {
        // Capture system output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Call show details
        documentary.showDetails();

        String output = outContent.toString();
        System.setOut(System.out);

        // Verify output
        assertTrue(output.contains("Título: Nature Exploration"), "Title should be present");
        assertTrue(output.contains("Duración en minutos: 60"), "Duration should be present");
        assertTrue(output.contains("Género: Nature"), "Genre should be present");
        assertTrue(output.contains("Tema: Marine Ecosystems"), "Topic should be present");
        assertTrue(Pattern.compile("ID: [A-Z]{2}\\d{3}-\\d").matcher(output).find(), 
            "ID should be present and follow the format");
    }

    @Test
    @DisplayName("Unimplemented Methods Throw UnsupportedOperationException")
    void testUnimplementedMethods() {
        assertThrows(UnsupportedOperationException.class, () -> documentary.getName());
        assertThrows(UnsupportedOperationException.class, () -> documentary.getContact());
        assertThrows(UnsupportedOperationException.class, () -> documentary.getLinkedInUser());
    }

    @Test
    @DisplayName("Researchers Can Be Modified")
    void testResearchersModification() {
        Researcher newResearcher = mock(Researcher.class);
        documentary.getResearchers().add(newResearcher);

        assertTrue(documentary.getResearchers().contains(newResearcher));
        assertEquals(3, documentary.getResearchers().size());
    }
}