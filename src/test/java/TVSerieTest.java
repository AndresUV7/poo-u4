import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import models.TVSerie;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class TVSerieTest {
    private TVSerie tvSerie;

    @BeforeEach
    void setUp() {
        // Create a TV Serie instance with some initial data
        tvSerie = TVSerie.builder()
                .title("Test TV Series")
                .build();
    }

    @Test
    @DisplayName("Add Season to TV Serie")
    void testAddSeason() {
        // Add multiple seasons
        tvSerie.addSeason(1, 10, "First season introduction", 2020);
        tvSerie.addSeason(2, 12, "Continuing the story", 2021);

        // Verify seasons were added
        Set<TVSerie.SerializableSeason> serializableSeasons = tvSerie.getSerializableSeasons();
        assertEquals(2, serializableSeasons.size());

        // Verify season details
        assertTrue(serializableSeasons.stream()
                .anyMatch(season -> 
                    season.getSeasonNumber() == 1 && 
                    season.getEpisodesNumber() == 10 && 
                    season.getYear() == 2020
                ));
    }

    @Test
    @DisplayName("Load Seasons from Serializable Seasons")
    void testLoadSeasonsFromSerializableSeasons() {
        // Prepare serializable seasons
        List<TVSerie.SerializableSeason> serializableSeasons = new ArrayList<>();
        serializableSeasons.add(new TVSerie.SerializableSeason(
            tvSerie.getId(), 1, 10, "First season introduction", 2020
        ));
        serializableSeasons.add(new TVSerie.SerializableSeason(
            tvSerie.getId(), 2, 12, "Continuing the story", 2021
        ));

        // Load seasons
        tvSerie.loadSeasonsFromSerializableSeasons(serializableSeasons);

        // Verify seasons were loaded
        String seasonsDescription = tvSerie.getSeasonsDescription();
        assertTrue(seasonsDescription.contains("Temporada 1: 10 episodios (2020) - First season introduction"));
        assertTrue(seasonsDescription.contains("Temporada 2: 12 episodios (2021) - Continuing the story"));
    }

    @Test
    @DisplayName("Get Seasons Description")
    void testGetSeasonsDescription() {
        // Add seasons
        tvSerie.addSeason(1, 10, "First season introduction", 2020);
        tvSerie.addSeason(2, 12, "Continuing the story", 2021);

        // Get seasons description
        String seasonsDescription = tvSerie.getSeasonsDescription();

        // Verify description format and content
        assertNotNull(seasonsDescription);
        assertTrue(seasonsDescription.startsWith("Temporadas:"));
        assertTrue(seasonsDescription.contains("Temporada 1: 10 episodios (2020) - First season introduction"));
        assertTrue(seasonsDescription.contains("Temporada 2: 12 episodios (2021) - Continuing the story"));
    }

    @Test
    @DisplayName("Show Details Prints Correct Information")
    void testShowDetails() {
        // Add seasons
        tvSerie.addSeason(1, 10, "First season introduction", 2020);
        tvSerie.addSeason(2, 12, "Continuing the story", 2021);

        // Capture system output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Call show details
        tvSerie.showDetails();

        String output = outContent.toString();
        System.setOut(System.out);

        // Verify output
        assertTrue(output.contains("Título: Test TV Series"), "Title should be present");
        assertTrue(output.contains("Temporadas:"), "Seasons section should be present");
        assertTrue(output.contains("Temporada 1: 10 episodios (2020) - First season introduction"), "First season details should be present");
        assertTrue(output.contains("Temporada 2: 12 episodios (2021) - Continuing the story"), "Second season details should be present");
        assertTrue(Pattern.compile("ID: [A-Z]{2}\\d{3}-\\d").matcher(output).find(), "ID should be present and follow the format");
    }

    @Test
    @DisplayName("No Seasons Scenario")
    void testNoSeasonsDescription() {
        // No seasons added
        String seasonsDescription = tvSerie.getSeasonsDescription();
        assertEquals("No hay temporadas registradas.", seasonsDescription);
    }

    @Test
    @DisplayName("Serializable Seasons Conversion")
    void testSerializableSeasonsConversion() {
        // Add seasons
        tvSerie.addSeason(1, 10, "First season introduction", 2020);
        tvSerie.addSeason(2, 12, "Continuing the story", 2021);

        // Get serializable seasons
        Set<TVSerie.SerializableSeason> serializableSeasons = tvSerie.getSerializableSeasons();

        // Verify conversion
        assertEquals(2, serializableSeasons.size());
        
        // Check each serializable season
        serializableSeasons.forEach(serializableSeason -> {
            assertEquals(tvSerie.getId(), serializableSeason.getSerieID());
            assertTrue(serializableSeason.getSeasonNumber() == 1 || serializableSeason.getSeasonNumber() == 2);
        });
    }
}