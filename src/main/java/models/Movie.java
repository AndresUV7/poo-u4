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
public class Movie extends AudiovisualContent {

    @CsvBindByName
    private String studio;
    private Set<Actor> actors;

    @Override
    public void showDetails() {
        printCommonDetails();
        System.out.println("Estudio: " + this.studio);
        System.out.println("------------------------------------");
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public String getContact() {
        throw new UnsupportedOperationException("Unimplemented method 'getContact'");
    }

    @Override
    public String getLinkedInUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLinkedInUser'");
    }
}
