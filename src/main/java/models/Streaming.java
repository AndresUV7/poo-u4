package models;

import java.util.Set;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class Streaming extends AudiovisualContent {

    @CsvBindByName
    private Set<String> platforms;
    @CsvBindByName
    private boolean isLive;

    public Set<String> getPlatforms() {
        return platforms;
    }
    public String getPlatformsList() {
        return String.join(", ", this.platforms);
    }

    public String getIsLive() {
        return this.isLive ? "Si" : "No";
    }

    @Override
    public void showDetails() {
        printCommonDetails();
        System.out.println("Plataformas: " + getPlatformsList());
        System.out.println("En vivo: " + getIsLive());
        System.out.println("------------------------------------");
    }

}
