import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import models.AudiovisualContent;
import views.AudiovisualContentView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AudiovisualContentViewTest {

    private AudiovisualContentView view;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Mock
    private AudiovisualContent mockContent;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        
        // Create view instance
        view = new AudiovisualContentView();
        
        // Redirect System.out to capture console output
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testContentInputsMap() {
        assertNotNull(view.contentInputs);
        assertEquals(3, view.contentInputs.size());
        assertTrue(view.contentInputs.containsKey("title"));
        assertTrue(view.contentInputs.containsKey("duration"));
        assertTrue(view.contentInputs.containsKey("genre"));
    }

    @Test
    void testShowMainMenu() {
        view.showMainMenu();
        String expectedOutput = "1. Peliculas\n2. Series de TV\n3. Documentales\n4. Anuncios Publicitarios\n5. Transmisiones\n6. Salir\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    void testShowContentTypeMenu() {
        view.showContentTypeMenu("Peliculas");
        String output = outContent.toString();
        assertTrue(output.contains("1. Listar Peliculas"));
        assertTrue(output.contains("2. Ver detalles de Peliculas"));
        assertTrue(output.contains("3. Agregar Peliculas"));
        assertTrue(output.contains("4. Eliminar Peliculas"));
        assertTrue(output.contains("5. Volver"));
    }

 
    @Test
    void testListAllWithContents() {
        // Prepare mock contents
        Set<AudiovisualContent> contents = new HashSet<>();
        
        // Create first mock content
        AudiovisualContent content1 = mock(AudiovisualContent.class);
        when(content1.getId()).thenReturn("1");
        when(content1.getTitle()).thenReturn("Movie 1");
        contents.add(content1);
        
        // Create second mock content
        AudiovisualContent content2 = mock(AudiovisualContent.class);
        when(content2.getId()).thenReturn("2");
        when(content2.getTitle()).thenReturn("Movie 2");
        contents.add(content2);

        // Call method
        view.listAll(contents, "Peliculas");
        
        // Verify output
        String output = outContent.toString().trim();
        assertTrue(output.contains("Lista de Peliculas(2):"));
        assertTrue(output.contains("1. Movie 1"));
        assertTrue(output.contains("2. Movie 2"));
    }

    @Test
    void testShowDetailWithNullContent() {
        view.showDetail(null);
        assertEquals("El contenido no existe.", outContent.toString().trim());
    }

    @Test
    void testShowDetailWithValidContent() {
        // Prepare mock content
        when(mockContent.getTitle()).thenReturn("Test Movie");
        
        // Mock the showDetails method to simulate adding details to output
        doAnswer(invocation -> {
            System.out.println("Mocked details");
            return null;
        }).when(mockContent).showDetails();

        // Call method
        view.showDetail(mockContent);
        
        // Verify output
        String output = outContent.toString().trim();
        assertTrue(output.contains("Detalles de Test Movie:"));
        assertTrue(output.contains("Mocked details"));
    }

    @Test
    void testInputsMapsExist() {
        // Verify that all input maps exist and are not null
        assertNotNull(view.contentInputs);
        assertNotNull(view.movieInputs);
        assertNotNull(view.tvSerieInputs);
        assertNotNull(view.seasonInputs);
        assertNotNull(view.documentaryInputs);
        assertNotNull(view.advertisingSpotInputs);
        assertNotNull(view.streamingInputs);
    }

    // Clean up after tests
    @org.junit.jupiter.api.AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
}