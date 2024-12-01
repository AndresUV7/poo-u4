package controllers;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import repositories.CsvManager;
import utils.InputUtils;
import models.Movie;
import views.AudiovisualContentView;
import views.PersonsView;

public class MoviesController {
    private static final CsvManager<Movie> csvManager = 
        new CsvManager<>(Movie.class, "src/main/resources/files/peliculas.csv");

    public static void listAll(AudiovisualContentView contentView) throws IOException {
        contentView.listAll(new LinkedHashSet<>(csvManager.readAll()), "peliculas");
    }

    public static void showDetail(AudiovisualContentView contentView, String movieId) throws IOException {
        Optional<Movie> movieOptional = csvManager.find(movie -> movie.getId().equals(movieId));
        contentView.showDetail(movieOptional.orElse(null));
    }

    public static void add(AudiovisualContentView contentView, PersonsView personsView) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NumberFormatException {
        Map<String, String> inputValuesMap = new LinkedHashMap<>();
        contentView.contentInputs.putAll(contentView.movieInputs);
        contentView.contentInputs.forEach((key, value) -> {
            System.out.println(value);
            if (key.equals("duration") || key.equals("actors_number")) {
                int numericValue = InputUtils.getPositiveIntegerInput();
                inputValuesMap.put(key, String.valueOf(numericValue));
            } else {
                inputValuesMap.put(key, InputUtils.getScanner().nextLine());
            }
        });

        csvManager.add(Movie.builder()
            .title(inputValuesMap.get("title"))
            .minutesDuration(Integer.parseInt(inputValuesMap.get("duration")))
            .genre(inputValuesMap.get("genre"))
            .studio(inputValuesMap.get("studio"))
            .build(), "movie");
    }

    public static void delete(String movieId) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvManager.delete(movie -> movie.getId().equals(movieId));
    }
}