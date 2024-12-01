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
public class Researcher extends Person implements RecordIdentifiable {

    @CsvBindByName
    private int yearsExperience;
    @CsvBindByName
    private String linkedInUser;
    

    @Override
    public void showDetails() {
        printCommonDetails();
        System.out.println("Experiencia: " + this.yearsExperience);
        System.out.println("LinkedIn: " + this.linkedInUser);
        System.out.println("------------------------------------");
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getLinkedInUser() {
        return linkedInUser;
    }

    @Override
    public String getContact() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contact'");
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'title'");
    }
}
