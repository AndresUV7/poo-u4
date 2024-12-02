package views;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import models.Person;

public class PersonsView {

    public Map<String, String> personInputs = new LinkedHashMap<>() {
        {
            put("name", "Ingrese el nombre");
            put("nationality", "Ingrese la nacionalidad");
            put("birthday", "Ingrese la fecha de nacimiento en formato YYYY-MM-DD");
        }
    };

    public Map<String, String> actorInputs = new LinkedHashMap<>() {
        {
            put("role", "Ingrese el contacto");
        }
    };

    public void showMainMenu() {
        System.out.println("""
                1. Actores
                2. Investigadores
                3. Volver
                """);
    }

    public void showTypeMenu(String personType) {
        System.out.printf("""
                1. Listar %ss
                2. Ver detalles de %s
                3. Agregar %s
                4. Actualizar %s
                5. Eliminar %s
                6. Volver
                """,
                personType, personType, personType,
                personType, personType);
    }

    public void list(Set<Person> persons) {
        if (persons.isEmpty()) {
            System.out.println("No hay personas disponibles.");
            return;
        }
        for (Person person : persons) {
            System.out.println(person.getId() + ". " + person.getName());
        }
    }

    public void showDetail(Person person) {
        if (person == null) {
            System.out.println("La persona no existe.");
            return;
        }
        person.showDetails();
    }
}
