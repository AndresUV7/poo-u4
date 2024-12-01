import models.SpotType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpotTypeTest {

    @Test
    void testSpotTypeEnumValues() {
        // Verificar que el enum tiene los valores esperados
        assertEquals(5, SpotType.values().length); // Verificamos que hay 5 valores en el enum

        assertTrue(SpotType.TELEVISION instanceof SpotType);
        assertTrue(SpotType.INTERNET instanceof SpotType);
        assertTrue(SpotType.RADIO instanceof SpotType);
        assertTrue(SpotType.IMPRESO instanceof SpotType);
        assertTrue(SpotType.EXTERIOR instanceof SpotType);
    }

    @Test
    void testSpotTypeValueOf() {
        // Verificar que los valores del enum pueden ser recuperados correctamente con valueOf
        assertEquals(SpotType.TELEVISION, SpotType.valueOf("TELEVISION"));
        assertEquals(SpotType.INTERNET, SpotType.valueOf("INTERNET"));
        assertEquals(SpotType.RADIO, SpotType.valueOf("RADIO"));
        assertEquals(SpotType.IMPRESO, SpotType.valueOf("IMPRESO"));
        assertEquals(SpotType.EXTERIOR, SpotType.valueOf("EXTERIOR"));
    }

    @Test
    void testSpotTypeInvalidValueOf() {
        // Verificar que se lanza una excepción al intentar obtener un valor que no existe
        assertThrows(IllegalArgumentException.class, () -> SpotType.valueOf("INVALIDO"));
    }

    @Test
    void testSpotTypeEnumToString() {
        // Verificar que el método toString devuelve el nombre correcto
        assertEquals("TELEVISION", SpotType.TELEVISION.toString());
        assertEquals("INTERNET", SpotType.INTERNET.toString());
        assertEquals("RADIO", SpotType.RADIO.toString());
        assertEquals("IMPRESO", SpotType.IMPRESO.toString());
        assertEquals("EXTERIOR", SpotType.EXTERIOR.toString());
    }
}
