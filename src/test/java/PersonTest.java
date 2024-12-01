import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.Person;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    // Concrete implementation of abstract Person for testing
    private static class ConcretePersonImpl extends Person {
        public ConcretePersonImpl(String name, String nationality, LocalDate birthday) {
            this.setName(name);
            this.setNationality(nationality);
            this.setBirthday(birthday);
        }

        @Override
        public void showDetails() {
            printCommonDetails();
        }
    }

    private ConcretePersonImpl person;

    @BeforeEach
    void setUp() {
        person = new ConcretePersonImpl(
            "John Doe", 
            "USA", 
            LocalDate.of(1990, 1, 1)
        );
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("John Doe", person.getName());
        assertEquals("USA", person.getNationality());
        assertEquals(LocalDate.of(1990, 1, 1), person.getBirthday());
    }

    @Test
    void testGenerateIdFormat() {
        String id = person.getId();
        
        // Verify ID format
        assertTrue(id.matches("P\\d{3}-\\d+"), 
            "ID should match format P[3-digit-number]-[counter]");
        
        // Verify ID starts with P
        assertTrue(id.startsWith("P"), "ID should start with 'P'");
    }

    @Test
    void testUniqueIdGeneration() {
        // Create multiple person instances
        Set<String> generatedIds = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            ConcretePersonImpl newPerson = new ConcretePersonImpl(
                "Test Person " + i, 
                "Test Nationality", 
                LocalDate.now()
            );
            
            // Ensure no duplicate IDs
            assertFalse(generatedIds.contains(newPerson.getId()), 
                "Generated ID should be unique");
            generatedIds.add(newPerson.getId());
        }
    }

    @Test
    void testPrintCommonDetails() {
        // Redirect system output to verify printing
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        // Call showDetails which internally calls printCommonDetails
        person.showDetails();

        // Restore standard output
        System.setOut(System.out);

        // Check printed output
        String printedOutput = outContent.toString();
        assertTrue(printedOutput.contains("ID: " + person.getId()), 
            "Output should contain person's ID");
        assertTrue(printedOutput.contains("Nombre: John Doe"), 
            "Output should contain person's name");
        assertTrue(printedOutput.contains("Nacionalidad: USA"), 
            "Output should contain person's nationality");
        assertTrue(printedOutput.contains("Fecha de nacimiento: 1990-01-01"), 
            "Output should contain person's birthday");
    }

    @Test
    void testIdComponentsIndependently() {
        // Verify random number component is within expected range
        String id = person.getId();
        String randomPart = id.substring(1, 4);
        int randomNumber = Integer.parseInt(randomPart);
        
        assertTrue(randomNumber >= 100 && randomNumber <= 999, 
            "Random part of ID should be between 100 and 999");

        // Verify counter component increases
        String firstId = new ConcretePersonImpl("First", "Country1", LocalDate.now()).getId();
        String secondId = new ConcretePersonImpl("Second", "Country2", LocalDate.now()).getId();
        
        String firstCounter = firstId.substring(firstId.indexOf('-') + 1);
        String secondCounter = secondId.substring(secondId.indexOf('-') + 1);
        
        assertTrue(Integer.parseInt(secondCounter) > Integer.parseInt(firstCounter), 
            "Counter should increase with each new instance");
    }
}