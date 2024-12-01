import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import models.Person;
import views.PersonsView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonsViewTest {

    private PersonsView view;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Mock
    private Person mockPerson;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        
        // Create view instance
        view = new PersonsView();
        
        // Redirect System.out to capture console output
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testPersonInputsMap() {
        assertNotNull(view.personInputs);
        assertEquals(3, view.personInputs.size());
        assertTrue(view.personInputs.containsKey("name"));
        assertTrue(view.personInputs.containsKey("nationality"));
        assertTrue(view.personInputs.containsKey("birthday"));
    }

    @Test
    void testShowMainMenu() {
        view.showMainMenu();
        String expectedOutput = "1. Actores\n2. Investigadores\n3. Volver\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    void testShowTypeMenu() {
        view.showTypeMenu("Actor");
        String output = outContent.toString();
        assertTrue(output.contains("1. Listar Actors"));
        assertTrue(output.contains("2. Ver detalles de Actor"));
        assertTrue(output.contains("3. Agregar Actor"));
        assertTrue(output.contains("4. Actualizar Actor"));
        assertTrue(output.contains("5. Eliminar Actor"));
        assertTrue(output.contains("6. Volver"));
    }

    @Test
    void testListarWithEmptySet() {
        Set<Person> emptySet = new HashSet<>();
        view.listar(emptySet);
        
        String expectedOutput = "No hay personas disponibles.";
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void testListarWithPersons() {
        // Prepare mock contents
        Set<Person> persons = new HashSet<>();
        
        // Create first mock person
        Person person1 = mock(Person.class);
        when(person1.getId()).thenReturn("1");
        when(person1.getName()).thenReturn("John Doe");
        persons.add(person1);
        
        // Create second mock person
        Person person2 = mock(Person.class);
        when(person2.getId()).thenReturn("2");
        when(person2.getName()).thenReturn("Jane Smith");
        persons.add(person2);

        // Call method
        view.listar(persons);
        
        // Verify output
        String output = outContent.toString().trim();
        assertTrue(output.contains("1. John Doe"));
        assertTrue(output.contains("2. Jane Smith"));
    }

    @Test
    void testMostrarDetalleWithNullPerson() {
        view.mostrarDetalle(null);
        assertEquals("La persona no existe.", outContent.toString().trim());
    }

    @Test
    void testMostrarDetalleWithValidPerson() {
        // Mock the showDetails method to simulate adding details to output
        doAnswer(invocation -> {
            System.out.println("Mocked person details");
            return null;
        }).when(mockPerson).showDetails();

        // Call method
        view.mostrarDetalle(mockPerson);
        
        // Verify output
        String output = outContent.toString().trim();
        assertEquals("Mocked person details", output);
    }

    @Test
    void testInputsMapsExist() {
        // Verify that both input maps exist and are not null
        assertNotNull(view.personInputs);
        assertNotNull(view.actorInputs);
    }

    // Clean up after tests
    @org.junit.jupiter.api.AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
}