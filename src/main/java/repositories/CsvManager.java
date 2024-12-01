package repositories;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import models.RecordIdentifiable;
import models.TVSerie;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CsvManager<T extends RecordIdentifiable> {
    private final Class<T> type;
    private final String filePath;
    private String seasonsFilePath;

    public CsvManager(Class<T> type, String filePath, String seasonsFilePath) {
        this.type = type;
        this.filePath = filePath;
        this.seasonsFilePath = seasonsFilePath;
    }

    public CsvManager(Class<T> type, String filePath) {
        this.type = type;
        this.filePath = filePath;
    }

    // Nuevo método para agregar temporadas a un archivo CSV separado
    private void addSeasons(TVSerie tvSerie)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        if (seasonsFilePath == null)
            return;

        // Leer las temporadas existentes
        List<TVSerie.SerializableSeason> existingSeasons = readSeasons();

        // Obtener las nuevas temporadas para agregar
        Set<TVSerie.SerializableSeason> newSeasons = tvSerie.getSerializableSeasons();

        // Agregar las nuevas temporadas
        existingSeasons.addAll(newSeasons);

        // Escribir todas las temporadas de vuelta al archivo
        writeSeasonsToFile(existingSeasons);
    }

    // Leer las temporadas del archivo CSV de temporadas
    public List<TVSerie.SerializableSeason> readSeasons() throws IOException {
        if (seasonsFilePath == null)
            return new ArrayList<>();

        try (Reader reader = new FileReader(seasonsFilePath)) {
            CsvToBean<TVSerie.SerializableSeason> csvToBean = new CsvToBeanBuilder<TVSerie.SerializableSeason>(reader)
                    .withType(TVSerie.SerializableSeason.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return new ArrayList<>(csvToBean.parse());
        } catch (FileNotFoundException e) {
            return new ArrayList<>(); // Retorna vacío si no se encuentra el archivo
        }
    }

    // Escribir las temporadas en el archivo CSV de temporadas
    private void writeSeasonsToFile(List<TVSerie.SerializableSeason> seasons)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        if (seasonsFilePath == null)
            return;

        try (Writer writer = new FileWriter(seasonsFilePath)) {
            StatefulBeanToCsv<TVSerie.SerializableSeason> beanToCsv = new StatefulBeanToCsvBuilder<TVSerie.SerializableSeason>(
                    writer).build();
            beanToCsv.write(seasons);
        }
    }

    // Crear o agregar un nuevo registro si el ID no existe
    public void add(T record, String type)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<T> records = readAll();

        // Verificar si el registro ya existe por su ID
        Optional<T> existingRecord;

        if (type.equals("actor")) {
            existingRecord = records.stream()
                    .filter(r -> r.getName().equals(record.getName()) && r.getContact().equals(record.getContact()))
                    .findFirst();
        } else if (type.equals("researcher")) {
            existingRecord = records.stream()
                    .filter(r -> r.getLinkedInUser().equals(record.getLinkedInUser()))
                    .findFirst();
        } else {
            existingRecord = records.stream()
                    .filter(r -> r.getTitle().equals(record.getTitle()))
                    .findFirst();
        }

        if (existingRecord.isPresent()) {
            // El registro ya existe, no se agrega.
            String customId = type.equals("actor") || type.equals("researcher")
                    ? "Persona" + record.getName()
                    : "Contenido" + record.getTitle();
            System.out.println(customId + " ya existe.");
            return;
        }

        if (record instanceof TVSerie) {
            TVSerie tvSerie = (TVSerie) record;
            addSeasons(tvSerie);
        }
        // Agregar el registro ya que no existe
        records.add(record);
        writeAll(records);
    }

    /**
     * Lee y convierte todos los registros de un archivo CSV en una lista de objetos
     * del tipo genérico `T`.
     * 
     * @return Una lista de objetos `T` leídos del archivo CSV. Retorna una lista
     *         vacía si el archivo no existe.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public List<T> readAll() throws IOException {
        List<T> records = new ArrayList<>(); // Inicializa una lista vacía para almacenar los registros.

        try (Reader reader = new FileReader(filePath)) { // Intenta abrir el archivo para lectura.
            // Configura y construye un CsvToBean para convertir el contenido del archivo
            // CSV a objetos de tipo `T`.
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true) // Ignora los espacios en blanco iniciales.
                    .build();
            records = new ArrayList<>(csvToBean.parse()); // Convierte y almacena los registros en la lista.
        } catch (FileNotFoundException e) {
            return records; // Retorna una lista vacía si el archivo no existe.
        }

        // Verifica si el tipo es `TVSerie` para cargar temporadas relacionadas.
        if (type == TVSerie.class) {
            List<TVSerie.SerializableSeason> allSeasons = readSeasons(); // Lee todas las temporadas serializadas.

            for (T record : records) { // Itera sobre cada registro de la lista.
                if (record instanceof TVSerie) { // Verifica si el registro es una instancia de `TVSerie`.
                    // Carga las temporadas a partir de las temporadas serializadas.
                    ((TVSerie) record).loadSeasonsFromSerializableSeasons(allSeasons);
                }
            }
        }

        return records; // Retorna la lista de registros procesados.
    }

    public Optional<T> find(Predicate<T> predicate) throws IOException {
        List<T> records = readAll();
        return records.stream()
                .filter(predicate)
                .findFirst();
    }

    // Actualizar un registro basado en un predicado
    public void update(Predicate<T> predicate, T updatedRecord)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<T> records = readAll();
        for (int i = 0; i < records.size(); i++) {
            if (predicate.test(records.get(i))) {
                records.set(i, updatedRecord);
                break;
            }
        }
        writeAll(records);
    }

    // Eliminar un registro basado en un predicado
    public void delete(Predicate<T> predicate)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<T> records = readAll();

        // Encontrar el registro(s) a eliminar
        Optional<T> recordToDelete = records.stream()
                .filter(predicate)
                .findFirst();

        if (recordToDelete.isPresent()) {
            T record = recordToDelete.get();

            // Si el registro es una TVSerie, eliminar sus temporadas asociadas
            if (record instanceof TVSerie) {
                TVSerie tvSerie = (TVSerie) record;
                deleteSeasons(tvSerie);
            }

            // Eliminar el registro de la lista principal
            records.removeIf(predicate);

            // Escribir los registros actualizados en el archivo
            writeAll(records);
        }
    }

    // Eliminar temporadas asociadas con una serie de TV
    private void deleteSeasons(TVSerie tvSerie)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        if (seasonsFilePath == null)
            return;

        // Leer las temporadas existentes
        List<TVSerie.SerializableSeason> existingSeasons = readSeasons();

        // Filtrar las temporadas asociadas con la serie de TV
        String seriesId = tvSerie.getId(); // Se asume que getId() identifica de manera única la serie de TV
        List<TVSerie.SerializableSeason> updatedSeasons = existingSeasons.stream()
                .filter(season -> !season.getSerieID().equals(seriesId))
                .collect(Collectors.toList()); // Usar Collectors.toList() en lugar de toList()

        // Escribir las temporadas restantes de vuelta al archivo
        writeSeasonsToFile(updatedSeasons);
    }

    /**
     * Escribe todos los registros proporcionados en un archivo CSV.
     * Si el archivo ya existe, será sobrescrito.
     * 
     * @param records Lista de objetos `T` que se escribirán en el archivo CSV.
     * @throws IOException                    Si ocurre un error al escribir en el
     *                                        archivo.
     * @throws CsvDataTypeMismatchException   Si existe un problema de coincidencia
     *                                        de tipos al convertir los datos.
     * @throws CsvRequiredFieldEmptyException Si algún campo obligatorio está vacío
     *                                        en los objetos `T`.
     */
    public void writeAll(List<T> records)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try (Writer writer = new FileWriter(filePath)) { // Abre el archivo para escritura.
            // Configura un StatefulBeanToCsv para escribir los registros en formato CSV.
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).build();
            beanToCsv.write(records); // Escribe la lista de registros en el archivo.
        }
    }

}
