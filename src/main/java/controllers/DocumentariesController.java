package controllers;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import repositories.CsvManager;
import models.Documentary;
import views.AudiovisualContentView;

public class DocumentariesController {
    private static final CsvManager<Documentary> csvManager = 
        new CsvManager<>(Documentary.class, "src/main/resources/files/documentales.csv");

    public static void listAll(AudiovisualContentView contentView) throws IOException {
        contentView.listAll(new LinkedHashSet<>(csvManager.readAll()), "documentales");
    }

    public static void showDetail(AudiovisualContentView contentView, String documentaryId) throws IOException {
        Optional<Documentary> documentaryOptional = csvManager.find(documentary -> documentary.getId().equals(documentaryId));
        contentView.showDetail(documentaryOptional.orElse(null));
    }

    public static void add() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvManager.add(Documentary.builder().title("Eclipse").minutesDuration(120).build(), "documentary");
    }

    public static void delete(String documentaryId) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvManager.delete(documentary -> documentary.getId().equals(documentaryId));
    }
}
