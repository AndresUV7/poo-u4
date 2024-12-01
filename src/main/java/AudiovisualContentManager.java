import java.io.IOException;
import java.util.Map;

import controllers.AdsController;
import controllers.DocumentariesController;
import controllers.MoviesController;
import controllers.StreamingController;
import controllers.TVSeriesController;
import utils.InputUtils;
import views.AudiovisualContentView;
import views.PersonsView;
import exceptions.InvalidContentException;
import exceptions.InvalidOperationException;

public class AudiovisualContentManager {
    private static final Map<Integer, String> CONTENT_OPTIONS = Map.of(
            1, "peliculas", 2, "series", 3, "documentales", 4, "anuncios", 5, "streaming");

    public static void main(String[] args) throws Exception {
        AudiovisualContentView contentView = new AudiovisualContentView();
        PersonsView personsView = new PersonsView();

        while (true) {
            int contentMenuInput = getValidContent(contentView);

            if (contentMenuInput == 6) {
                System.out.println("Programa finalizado.");
                InputUtils.closeScanner();
                System.exit(0);
            }

            while (true) {
                int operationMenuInput = getValidOperation(contentView, CONTENT_OPTIONS.get(contentMenuInput));

                if (operationMenuInput == 5) {
                    break;
                }

                try {
                    switch (operationMenuInput) {
                        case 1 -> {
                            switch (contentMenuInput) {
                                case 1 -> MoviesController.listAll(contentView);
                                case 2 -> TVSeriesController.listAll(contentView);
                                case 3 -> DocumentariesController.listAll(contentView);
                                case 4 -> AdsController.listAll(contentView);
                                case 5 -> StreamingController.listAll(contentView);
                            }
                        }
                        case 2 -> {
                            System.out.println("Ingrese el codigo: ");
                            String id = InputUtils.getScanner().nextLine();
                            switch (contentMenuInput) {
                                case 1 -> MoviesController.showDetail(contentView, id);
                                case 2 -> TVSeriesController.showDetail(contentView, id);
                                case 3 -> DocumentariesController.showDetail(contentView, id);
                                case 4 -> AdsController.showDetail(contentView, id);
                                case 5 -> StreamingController.showDetail(contentView, id);
                            }
                        }
                        case 3 -> {
                            switch (contentMenuInput) {
                                case 1 -> MoviesController.add(contentView, personsView);
                                case 2 -> TVSeriesController.add(contentView);
                                case 3 -> DocumentariesController.add();
                                case 4 -> AdsController.add();
                                case 5 -> StreamingController.add();
                            }
                        }
                        case 4 -> {
                            System.out.println("Ingrese el codigo: ");
                            String id = InputUtils.getScanner().nextLine();
                            switch (contentMenuInput) {
                                case 1 -> MoviesController.delete(id);
                                case 2 -> TVSeriesController.delete(id);
                                case 3 -> DocumentariesController.delete(id);
                                case 4 -> AdsController.delete(id);
                                case 5 -> StreamingController.delete(id);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al procesar la operación: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Solicita al usuario una opción válida del menú principal mostrado en la
     * vista.
     * 
     * @param contentView La vista que muestra el menú principal.
     * @return La opción válida seleccionada por el usuario.
     * @throws InvalidContentException Si la entrada no es válida.
     */
    private static int getValidContent(AudiovisualContentView contentView) {
        contentView.showMainMenu(); // Muestra el menú principal al usuario.
        // Solicita una opción válida entre 1 y 6, manejando entradas inválidas.
        return InputUtils.getUserChoice(1, 6,
                "Entrada inválida. Debe ingresar un número de la lista opciones.",
                InvalidContentException.class);
    }

    /**
     * Solicita al usuario una operación válida del menú basado en el tipo de
     * contenido.
     * 
     * @param contentView La vista que muestra el menú del tipo de contenido.
     * @param contentType El tipo de contenido para personalizar el menú (e.g.,
     *                    película, serie).
     * @return La operación válida seleccionada por el usuario.
     * @throws InvalidOperationException Si la entrada no es válida.
     */
    private static int getValidOperation(AudiovisualContentView contentView, String contentType) {
        contentView.showContentTypeMenu(contentType); // Muestra el menú para el tipo de contenido.
        // Solicita una opción válida entre 1 y 5, manejando entradas inválidas.
        return InputUtils.getUserChoice(1, 5,
                "Entrada inválida. Debe ingresar un número de la lista opciones.",
                InvalidOperationException.class);
    }
}