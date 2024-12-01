import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import models.RecordIdentifiable;
import models.TVSerie;
import repositories.CsvManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

class CsvManagerTest {

    private CsvManager<TVSerie> csvManager;

    @Mock
    private TVSerie mockTVSerie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializar el CsvManager con archivos ficticios
        csvManager = new CsvManager<>(TVSerie.class, "test_series.csv", "test_temporadas.csv");

        // Configurar un mock de TVSerie
        when(mockTVSerie.getId()).thenReturn("S001");
        when(mockTVSerie.getTitle()).thenReturn("Test Serie");
        when(mockTVSerie.getSerializableSeasons()).thenReturn(new HashSet<>());
    }

    @Test
    void testAddNewRecord() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // Mockear la lectura inicial del archivo (vacío)
        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(new ArrayList<>()).when(spyCsvManager).readAll();

        // Agregar un nuevo registro
        spyCsvManager.add(mockTVSerie, "tvserie");

        // Verificar que se escribió el nuevo registro
        verify(spyCsvManager, times(1)).writeAll(anyList());
    }

    @Test
    void testAddDuplicateRecord() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // Mockear un archivo con un registro existente
        List<TVSerie> existingRecords = new ArrayList<>();
        existingRecords.add(mockTVSerie);

        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(existingRecords).when(spyCsvManager).readAll();

        // Intentar agregar un registro duplicado
        spyCsvManager.add(mockTVSerie, "tvserie");

        // Verificar que no se escribió nada
        verify(spyCsvManager, never()).writeAll(anyList());
    }

    @Test
    void testReadAll() throws IOException {
        // Mockear la lectura del archivo
        List<TVSerie> records = new ArrayList<>();
        records.add(mockTVSerie);

        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(records).when(spyCsvManager).readAll();

        // Leer registros
        List<TVSerie> result = spyCsvManager.readAll();

        // Verificar que se devuelven los registros esperados
        assertEquals(1, result.size());
        assertEquals("S001", result.get(0).getId());
    }

    @Test
    void testUpdateRecord() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // Mockear un archivo con un registro existente
        List<TVSerie> records = new ArrayList<>();
        records.add(mockTVSerie);

        TVSerie updatedSerie = mock(TVSerie.class);
        when(updatedSerie.getId()).thenReturn("S001");
        when(updatedSerie.getTitle()).thenReturn("Updated Serie");

        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(records).when(spyCsvManager).readAll();

        // Actualizar el registro
        spyCsvManager.update(tvSerie -> tvSerie.getId().equals("S001"), updatedSerie);

        // Verificar que se escribió el registro actualizado
        verify(spyCsvManager, times(1)).writeAll(anyList());
        assertEquals("Updated Serie", records.get(0).getTitle());
    }

    @Test
    void testDeleteRecord() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // Mockear un archivo con un registro existente
        List<TVSerie> records = new ArrayList<>();
        records.add(mockTVSerie);

        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(records).when(spyCsvManager).readAll();

        // Eliminar el registro
        spyCsvManager.delete(tvSerie -> tvSerie.getId().equals("S001"));

        // Verificar que se escribió el archivo sin el registro eliminado
        verify(spyCsvManager, times(1)).writeAll(anyList());
        assertTrue(records.isEmpty());
    }

    @Test
    void testFindRecord() throws IOException {
        // Mockear un archivo con un registro existente
        List<TVSerie> records = new ArrayList<>();
        records.add(mockTVSerie);

        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(records).when(spyCsvManager).readAll();

        // Buscar un registro
        Optional<TVSerie> result = spyCsvManager.find(tvSerie -> tvSerie.getId().equals("S001"));

        // Verificar que se encontró el registro
        assertTrue(result.isPresent());
        assertEquals("S001", result.get().getId());
    }

    @Test
    void testReadAllEmptyFile() throws IOException {
        // Mockear un archivo vacío
        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(new ArrayList<>()).when(spyCsvManager).readAll();

        // Llamar al método readAll
        List<TVSerie> result = spyCsvManager.readAll();

        // Verificar que la lista está vacía
        assertTrue(result.isEmpty());
    }

    @Test
    void testReadAllWithMultipleRecords() throws IOException {
        // Mockear varios registros
        TVSerie serie1 = mock(TVSerie.class);
        when(serie1.getId()).thenReturn("S001");
        TVSerie serie2 = mock(TVSerie.class);
        when(serie2.getId()).thenReturn("S002");

        List<TVSerie> mockRecords = List.of(serie1, serie2);

        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(mockRecords).when(spyCsvManager).readAll();

        // Llamar al método readAll
        List<TVSerie> result = spyCsvManager.readAll();

        // Verificar que devuelve todos los registros
        assertEquals(2, result.size());
        assertEquals("S001", result.get(0).getId());
        assertEquals("S002", result.get(1).getId());
    }

    @Test
    void testAddToEmptyFile() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // Mockear un archivo vacío
        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(new ArrayList<>()).when(spyCsvManager).readAll();

        // Llamar al método add
        spyCsvManager.add(mockTVSerie, "tvserie");

        // Verificar que se escribió el nuevo registro
        verify(spyCsvManager, times(1)).writeAll(anyList());
    }

    @Test
    void testAddNullRecord() {
        // Llamar al método add con un registro nulo
        assertThrows(NullPointerException.class, () -> csvManager.add(null, "tvserie"));
    }

    @Test
    void testAddDuplicateWithCustomCheck()
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // Mockear una lista con un registro existente
        TVSerie existingTVSerie = mock(TVSerie.class);
        when(existingTVSerie.getTitle()).thenReturn("Test Serie");

        List<TVSerie> existingRecords = new ArrayList<>();
        existingRecords.add(existingTVSerie);

        CsvManager<TVSerie> spyCsvManager = spy(csvManager);
        doReturn(existingRecords).when(spyCsvManager).readAll();

        // Intentar añadir un registro con el mismo título
        spyCsvManager.add(mockTVSerie, "tvserie");

        // Verificar que no se escribió nada porque ya existe un registro con el mismo
        // título
        verify(spyCsvManager, never()).writeAll(anyList());
    }

}
