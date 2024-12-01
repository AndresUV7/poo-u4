package models;

import java.time.LocalDate;
import java.util.Random;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public abstract class Person {
    @CsvBindByName
    private final String id = generateId();
    @CsvBindByName
    private String name;
    @CsvBindByName
    private String nationality;
    @CsvBindByName
    private LocalDate birthday;

    private static int counter = 0;
    private static final Random random = new Random();

    private static synchronized String generateId() {
        int randomNumber = random.nextInt(900) + 100;
        return "P" + randomNumber + "-" + ++counter;
    }

    protected void printCommonDetails() {
        System.out.println("------------------------------------");
        System.out.println("ID: " + getId());
        System.out.println("Nombre: " + getName());
        System.out.println("Nacionalidad: " + getNationality());
        System.out.println("Fecha de nacimiento: " + getBirthday());
    }

    public abstract void showDetails();
}
