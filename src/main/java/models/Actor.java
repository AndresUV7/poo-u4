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
public class Actor extends Person implements RecordIdentifiable {

    @CsvBindByName
    private String contact;
    @Override
    public void showDetails() {
        printCommonDetails();
        System.out.println("Contacto: " + this.contact);
        System.out.println("------------------------------------");
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getContact() {
        return contact;
    }

    @Override
    public String getLinkedInUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLinkedInUser'");
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'title'");
    }
}
