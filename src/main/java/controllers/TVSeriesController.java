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
import models.TVSerie;
import views.AudiovisualContentView;

public class TVSeriesController {
    private static final CsvManager<TVSerie> csvManager = 
        new CsvManager<>(TVSerie.class, 
            "src/main/resources/files/series.csv",
            "src/main/resources/files/temporadas.csv");

    public static void listAll(AudiovisualContentView contentView) throws IOException {
        contentView.listAll(new LinkedHashSet<>(csvManager.readAll()), "series");
    }

    public static void showDetail(AudiovisualContentView contentView, String serieId) throws IOException {
        Optional<TVSerie> serieOptional = csvManager.find(serie -> serie.getId().equals(serieId));
        contentView.showDetail(serieOptional.orElse(null));
    }

    public static void add(AudiovisualContentView contentView) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Map<String, String> inputValuesMap = new LinkedHashMap<>();
        contentView.contentInputs.forEach((key, value) -> {
            System.out.println(value);
            if (key.equals("duration")) {
                int numericValue = InputUtils.getPositiveIntegerInput();
                inputValuesMap.put(key, String.valueOf(numericValue));
            } else {
                inputValuesMap.put(key, InputUtils.getScanner().nextLine());
            }
        });

        TVSerie serie = TVSerie.builder()
            .title(inputValuesMap.get("title"))
            .minutesDuration(Integer.parseInt(inputValuesMap.get("duration")))
            .genre(inputValuesMap.get("genre"))
            .build();

        System.out.println(contentView.tvSerieInputs.get("seasons_number"));
        int seasonCount = InputUtils.getPositiveIntegerInput();
        
        for (int i = 1; i <= seasonCount; i++) {
            Map<String, String> seasonMap = new LinkedHashMap<>();
            contentView.seasonInputs.forEach((key2, value2) -> {
                System.out.println(value2);
                if (key2.equals("description")) {
                    seasonMap.put(key2, InputUtils.getScanner().nextLine());
                } else {
                    int numericValue2 = InputUtils.getPositiveIntegerInput();
                    seasonMap.put(key2, String.valueOf(numericValue2));
                }
            });

            serie.addSeason(
                Integer.parseInt(seasonMap.get("season_number")),
                Integer.parseInt(seasonMap.get("episodes_number")),
                seasonMap.get("description"),
                Integer.parseInt(seasonMap.get("year"))
            );
        }

        csvManager.add(serie, "serie");
    }

    public static void delete(String seriesId) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvManager.delete(series -> series.getId().equals(seriesId));
    }
}