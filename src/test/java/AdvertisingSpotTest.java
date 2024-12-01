import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import models.AdvertisingSpot;
import models.SpotType;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class AdvertisingSpotTest {
    private AdvertisingSpot advertisingSpot;

    @BeforeEach
    void setUp() {
        // Create advertising spot
        advertisingSpot = AdvertisingSpot.builder()
                .title("Product Launch Commercial")
                .minutesDuration(30)
                .genre("Advertisement")
                .brand("TechCorp")
                .spotType(SpotType.INTERNET)
                .build();
    }

    @Test
    @DisplayName("AdvertisingSpot Builder Creates Instance Correctly")
    void testAdvertisingSpotBuilder() {
        assertNotNull(advertisingSpot);
        assertEquals("Product Launch Commercial", advertisingSpot.getTitle());
        assertEquals(30, advertisingSpot.getMinutesDuration());
        assertEquals("Advertisement", advertisingSpot.getGenre());
        assertEquals("TechCorp", advertisingSpot.getBrand());
        assertEquals(SpotType.INTERNET, advertisingSpot.getSpotType());
    }

    @Test
    @DisplayName("Show Details Prints Correct Information")
    void testShowDetails() {
        // Capture system output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Call show details
        advertisingSpot.showDetails();

        String output = outContent.toString();
        System.setOut(System.out);

        // Verify output
        assertTrue(output.contains("Título: Product Launch Commercial"), "Title should be present");
        assertTrue(output.contains("Duración en minutos: 30"), "Duration should be present");
        assertTrue(output.contains("Género: Advertisement"), "Genre should be present");
        assertTrue(output.contains("Marca: TechCorp"), "Brand should be present");
        assertTrue(output.contains("Tipo: INTERNET"), "Spot type should be present");
        assertTrue(Pattern.compile("ID: [A-Z]{2}\\d{3}-\\d").matcher(output).find(), 
            "ID should be present and follow the format");
    }

    @Test
    @DisplayName("Unimplemented Methods Throw UnsupportedOperationException")
    void testUnimplementedMethods() {
        assertThrows(UnsupportedOperationException.class, () -> advertisingSpot.getName());
        assertThrows(UnsupportedOperationException.class, () -> advertisingSpot.getContact());
        assertThrows(UnsupportedOperationException.class, () -> advertisingSpot.getLinkedInUser());
    }
}