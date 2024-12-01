package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Locale;

public class InputUtils {
    private static final Scanner input = new Scanner(System.in).useLocale(Locale.US);

    /**
     * Solicita al usuario una opción numérica válida dentro de un rango.
     * Repite hasta obtener una entrada correcta.
     * 
     * @param min            Valor mínimo permitido.
     * @param max            Valor máximo permitido.
     * @param errorMessage   Mensaje de error para entradas inválidas.
     * @param exceptionClass Clase de la excepción a lanzar en caso de error.
     * @return La opción válida seleccionada por el usuario.
     */
    public static int getUserChoice(int min, int max, String errorMessage, Class<? extends Exception> exceptionClass) {
        while (true) {
            try {
                if (input.hasNextInt()) { // Verifica si la entrada es un número entero.
                    int choice = input.nextInt();
                    if (choice >= min && choice <= max) { // Verifica si el número está en el rango permitido.
                        input.nextLine(); // Limpia el buffer.
                        return choice; // Retorna la elección válida.
                    }
                }
                // Lanza una excepción personalizada si la entrada no es válida.
                throw exceptionClass.getConstructor(String.class).newInstance(errorMessage);
            } catch (Exception e) {
                System.out.println(e.getMessage()); // Muestra el mensaje de error.
                input.nextLine(); // Limpia el buffer en caso de entrada inválida.
            }
        }
    }

    public static String getValidDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.println("Ingrese una fecha en formato YYYY-MM-DD: ");
            String dateInput = input.nextLine();
            try {
                LocalDate date = LocalDate.parse(dateInput, formatter);
                return date.toString();
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Debe estar en formato YYYY-MM-DD. Intente nuevamente.");
            }
        }
    }

    public static int getPositiveIntegerInput() {
        int value;
        while (true) {
            if (input.hasNextInt()) {
                value = input.nextInt();
                input.nextLine();
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("El número debe ser positivo. Intente nuevamente.");
                }
            } else {
                System.out.println("Por favor, ingrese un valor numérico válido.");
                input.nextLine();
            }
        }
    }

    public static Scanner getScanner() {
        return input;
    }

    public static void closeScanner() {
        input.close();
    }
}