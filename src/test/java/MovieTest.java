import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import models.Actor;
import models.Movie;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieTest {
    private Movie movie;
    private Set<Actor> actors;

    @BeforeEach
    void setUp() {
        // Create a mock set of actors
        actors = new HashSet<>();
        Actor actor1 = mock(Actor.class);
        Actor actor2 = mock(Actor.class);
        actors.add(actor1);
        actors.add(actor2);

        // Use the builder to create a Movie instance
        movie = Movie.builder()
                .title("Test Movie")
                .studio("Test Studio")
                .actors(actors)
                .build();
    }

    @Test
    @DisplayName("Movie Builder Creates Instance Correctly")
    void testMovieBuilder() {
        assertNotNull(movie);
        assertEquals("Test Movie", movie.getTitle());
        assertEquals("Test Studio", movie.getStudio());
        assertEquals(actors, movie.getActors());
    }

    @Test
    @DisplayName("Validate ID Generation Pattern")
    void testIdGenerationPattern() {
        // Capture the ID from showDetails output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        movie.showDetails();

        String output = outContent.toString();
        System.setOut(System.out);

        // Extract ID using regex
        java.util.regex.Matcher matcher = Pattern.compile("ID: ([A-Z]{2}\\d{3}-\\d)").matcher(output);
        
        assertTrue(matcher.find(), "ID not found in output");
        String extractedId = matcher.group(1);

        // Validate ID format
        assertTrue(extractedId.matches("[A-Z]{2}\\d{3}-\\d"), 
            "ID does not match expected format: " + extractedId);
    }

    @Test
    @DisplayName("Show Details Contains Expected Information")
    void testShowDetailsContent() {
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        movie.showDetails();

        String output = outContent.toString();
        System.setOut(System.out);

        // Check for key content
        assertTrue(output.contains("Título: Test Movie"), "Title not found");
        assertTrue(output.contains("Estudio: Test Studio"), "Studio not found");
        assertTrue(output.contains("Duración en minutos: 0"), "Duration not found");
        assertTrue(output.contains("Género: null"), "Genre not found");
        assertTrue(Pattern.compile("ID: [A-Z]{2}\\d{3}-\\d").matcher(output).find(), 
            "ID format invalid");
    }

    @Test
    @DisplayName("Repeated Calls Generate Consistent ID Format")
    void testIdConsistency() {
        java.io.ByteArrayOutputStream outContent1 = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent1));
        movie.showDetails();
        String output1 = outContent1.toString();
        System.setOut(System.out);

        java.io.ByteArrayOutputStream outContent2 = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent2));
        movie.showDetails();
        String output2 = outContent2.toString();
        System.setOut(System.out);

        // Extract IDs
        java.util.regex.Matcher matcher1 = Pattern.compile("ID: ([A-Z]{2}\\d{3}-\\d)").matcher(output1);
        java.util.regex.Matcher matcher2 = Pattern.compile("ID: ([A-Z]{2}\\d{3}-\\d)").matcher(output2);

        assertTrue(matcher1.find(), "First ID not found");
        assertTrue(matcher2.find(), "Second ID not found");

        // Validate ID format consistency
        String id1 = matcher1.group(1);
        String id2 = matcher2.group(1);

        assertTrue(id1.matches("[A-Z]{2}\\d{3}-\\d"), "First ID format invalid");
        assertTrue(id2.matches("[A-Z]{2}\\d{3}-\\d"), "Second ID format invalid");
    }
    @Test
    @DisplayName("Unimplemented Methods Throw UnsupportedOperationException")
    void testUnimplementedMethods() {
        assertThrows(UnsupportedOperationException.class, () -> movie.getName());
        assertThrows(UnsupportedOperationException.class, () -> movie.getContact());
        assertThrows(UnsupportedOperationException.class, () -> movie.getLinkedInUser());
    }

    @Test
    @DisplayName("Actors Can Be Modified")
    void testActorsModification() {
        Actor newActor = mock(Actor.class);
        movie.getActors().add(newActor);

        assertTrue(movie.getActors().contains(newActor));
        assertEquals(3, movie.getActors().size());
    }
}