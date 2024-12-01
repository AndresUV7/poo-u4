package controllers;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import repositories.CsvManager;
import models.AdvertisingSpot;
import views.AudiovisualContentView;

public class AdsController {
    private static final CsvManager<AdvertisingSpot> csvManager = 
        new CsvManager<>(AdvertisingSpot.class, "src/main/resources/files/anuncios.csv");

    public static void listAll(AudiovisualContentView contentView) throws IOException {
        contentView.listAll(new LinkedHashSet<>(csvManager.readAll()), "anuncios");
    }

    public static void showDetail(AudiovisualContentView contentView, String adId) throws IOException {
        Optional<AdvertisingSpot> adOptional = csvManager.find(ad -> ad.getId().equals(adId));
        contentView.showDetail(adOptional.orElse(null));
    }

    public static void add() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvManager.add(AdvertisingSpot.builder().title("Eclipse").minutesDuration(120).build(), "ad");
    }

    public static void delete(String adId) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvManager.delete(ad -> ad.getId().equals(adId));
    }
}