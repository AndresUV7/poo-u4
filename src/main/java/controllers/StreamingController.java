package controllers;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import repositories.CsvManager;
import models.Streaming;
import views.AudiovisualContentView;

public class StreamingController {
    private static final CsvManager<Streaming> csvManager = 
        new CsvManager<>(Streaming.class, "src/main/resources/files/transmisiones.csv");

    public static void listAll(AudiovisualContentView contentView) throws IOException {
        contentView.listAll(new LinkedHashSet<>(csvManager.readAll()), "streaming");
    }

    public static void showDetail(AudiovisualContentView contentView, String streamingId) throws IOException {
        Optional<Streaming> streamingOptional = csvManager.find(streaming -> streaming.getId().equals(streamingId));
        contentView.showDetail(streamingOptional.orElse(null));
    }

    public static void add() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvManager.add(Streaming.builder().title("Eclipse").minutesDuration(120).build(), "stream");
    }

    public static void delete(String streamingId) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvManager.delete(streaming -> streaming.getId().equals(streamingId));
    }
}