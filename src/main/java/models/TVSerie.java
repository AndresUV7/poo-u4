package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TVSerie extends AudiovisualContent {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SerializableSeason {
        @CsvBindByName
        private String serieID;
        
        @CsvBindByName
        private int seasonNumber;
        
        @CsvBindByName
        private int episodesNumber;
        
        @CsvBindByName
        private String description;
        
        @CsvBindByName
        private int year;
    }

    @Data
    @AllArgsConstructor
    private class Season {
        private int number;
        private int episodesNumber;
        private String description;
        private int year;
    }

    @Builder.Default
    private transient Set<Season> seasons = new HashSet<>();


    public void addSeason(int number, int episodesNumber, String description, int year) {
        Season newSeason = new Season(number, episodesNumber, description, year);
        seasons.add(newSeason);
    }

    // Method to convert internal seasons to serializable seasons
    public Set<SerializableSeason> getSerializableSeasons() {
        Set<SerializableSeason> serializableSeasons = new HashSet<>();
        for (Season season : seasons) {
            serializableSeasons.add(new SerializableSeason(
                this.getId(), 
                season.getNumber(), 
                season.getEpisodesNumber(), 
                season.getDescription(), 
                season.getYear()
            ));
        }
        return serializableSeasons;
    }

    
    private void printSeasons() {
        System.out.println("Temporadas:");
        List<Season> seasonsList = new ArrayList<>(seasons);
        Collections.sort(seasonsList, Comparator.comparingInt(Season::getNumber));

        for (Season temp : seasonsList) {
            System.out.println("\tTemporada " + temp.getNumber() +
                    ": " + temp.getEpisodesNumber() + " episodios" +
                    " (" + temp.getYear() + ") - " + temp.getDescription());
        }
    }


    // Método para cargar temporadas desde una lista de SerializableSeason
    public void loadSeasonsFromSerializableSeasons(List<SerializableSeason> serializableSeasons) {
        // Limpiar temporadas existentes
        this.seasons.clear();
        
        // Filtrar solo las temporadas de esta serie específica
        List<SerializableSeason> thisSeriesSeasons = serializableSeasons.stream()
            .filter(season -> season.getSerieID().equals(this.getId()))
            .collect(Collectors.toList());
        
        // Convertir SerializableSeason a Season
        for (SerializableSeason serializableSeason : thisSeriesSeasons) {
            this.addSeason(
                serializableSeason.getSeasonNumber(), 
                serializableSeason.getEpisodesNumber(), 
                serializableSeason.getDescription(), 
                serializableSeason.getYear()
            );
        }
    }

    // Método para obtener la descripción de las temporadas
    public String getSeasonsDescription() {
        if (seasons.isEmpty()) {
            return "No hay temporadas registradas.";
        }

        StringBuilder sb = new StringBuilder("Temporadas:\n");
        List<Season> seasonsList = new ArrayList<>(seasons);
        Collections.sort(seasonsList, Comparator.comparingInt(Season::getNumber));
        
        for (Season temp : seasonsList) {
            sb.append(String.format("\tTemporada %d: %d episodios (%d) - %s\n", 
                temp.getNumber(), 
                temp.getEpisodesNumber(), 
                temp.getYear(), 
                temp.getDescription()));
        }
        return sb.toString();
    }

    @Override
    public void showDetails() {
        printCommonDetails();
        System.out.println(getSeasonsDescription());
        System.out.println("------------------------------------");
    }

    // @Override
    // public void showDetails() {
    //     printCommonDetails();
    //     printSeasons();
    //     System.out.println("------------------------------------");
    // }

}