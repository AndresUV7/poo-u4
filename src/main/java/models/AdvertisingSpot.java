package models;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AdvertisingSpot extends AudiovisualContent {
    
    @CsvBindByName
    private String brand;
    @CsvBindByName
    private SpotType spotType;

    @Override
    public void showDetails() {
        printCommonDetails();
        System.out.println("Marca: " + this.brand);
        System.out.println("Tipo: " + this.spotType);
        System.out.println("------------------------------------");
    }
}
