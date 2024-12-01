package models;

import java.util.Set;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Documentary extends AudiovisualContent {
    
    @CsvBindByName
    private String topic;

    private Set<Researcher> researchers;

    @Override
    public void showDetails() {
        printCommonDetails();
        System.out.println("Tema: " + this.topic);
        System.out.println("------------------------------------");
    }

}
