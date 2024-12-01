import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.Actor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ActorTest {
    private Actor actor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        // Redirigir System.out para capturar el contenido impreso
        System.setOut(new PrintStream(outContent));

        // Crear un actor de prueba
        actor = Actor.builder()
                .name("Tom Hanks")
                .nationality("USA")
                .birthday(LocalDate.of(1956, 7, 9))
                .contact("tom.hanks@example.com")
                .build();
    }

    @Test
    public void testShowDetails() {
        // Llamar al método que imprime los detalles
        actor.showDetails();

        // Obtener el id generado dinámicamente
        String generatedId = actor.getId();

        // Construir la salida esperada
        String expectedOutput = """
                ------------------------------------
                ID: %s
                Nombre: Tom Hanks
                Nacionalidad: USA
                Fecha de nacimiento: 1956-07-09
                Contacto: tom.hanks@example.com
                ------------------------------------
                """.formatted(generatedId).trim();

        // Normalizar saltos de línea en ambas cadenas
        String actualOutput = outContent.toString().trim().replace("\r\n", "\n").replace("\r", "\n");
        String normalizedExpected = expectedOutput.replace("\r\n", "\n").replace("\r", "\n");

        // Verificar la salida generada
        assertEquals(normalizedExpected, actualOutput, "La salida del método showDetails no coincide con lo esperado");
    }

    @Test
    public void testActorConstructorAndGetters() {
        assertEquals("Tom Hanks", actor.getName());
        assertEquals("USA", actor.getNationality());
        assertEquals(LocalDate.of(1956, 7, 9), actor.getBirthday());
        assertEquals("tom.hanks@example.com", actor.getContact());
        assertNotNull(actor.getId(), "El ID no debería ser nulo");
    }

    @AfterEach
    public void tearDown() {
        // Restaurar System.out después de las pruebas
        System.setOut(originalOut);
    }
}
