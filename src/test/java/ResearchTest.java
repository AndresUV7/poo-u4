import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.Researcher;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ResearcherTest {
    private Researcher researcher;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture printed content
        System.setOut(new PrintStream(outContent));

        // Create a test researcher
        researcher = Researcher.builder()
                .name("John Doe")
                .nationality("USA")
                .birthday(LocalDate.of(1980, 5, 15))
                .linkedInUser("johndoe")
                .yearsExperience(10)
                .build();
    }

    @Test
    void testResearcherConstructorAndGetters() {
        assertEquals("John Doe", researcher.getName());
        assertEquals("USA", researcher.getNationality());
        assertEquals(LocalDate.of(1980, 5, 15), researcher.getBirthday());
        assertEquals("johndoe", researcher.getLinkedInUser());
        assertEquals(10, researcher.getYearsExperience());
    }

    @Test
    void testShowDetails() {
        // Call the method that prints details
        researcher.showDetails();

        // Obtener el ID generado dinámicamente
        String generatedId = researcher.getId();

        // Construir la salida esperada dinámicamente
        String expectedOutput = """
                ------------------------------------
                ID: %s
                Nombre: John Doe
                Nacionalidad: USA
                Fecha de nacimiento: 1980-05-15
                Experiencia: 10
                LinkedIn: johndoe
                ------------------------------------
                """.formatted(generatedId);

        // Normalizar saltos de línea en ambas cadenas
        String actualOutput = outContent.toString().trim().replace("\r\n", "\n").replace("\r", "\n");
        String normalizedExpected = expectedOutput.trim().replace("\r\n", "\n").replace("\r", "\n");

        // Verificar la salida generada
        assertEquals(normalizedExpected, actualOutput, "La salida del método showDetails no coincide con lo esperado");
    }

    @Test
    void testGetName() {
        assertEquals("John Doe", researcher.getName());
    }

    @Test
    void testGetLinkedInUser() {
        assertEquals("johndoe", researcher.getLinkedInUser());
    }

    @Test
    void testGetContactAndGetTitleThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, researcher::getContact);
        assertThrows(UnsupportedOperationException.class, researcher::getTitle);
    }

    @AfterEach
    void tearDown() {
        // Restore the original System.out
        System.setOut(originalOut);
    }
}