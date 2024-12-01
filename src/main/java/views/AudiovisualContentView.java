package views;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.AudiovisualContent;

public class AudiovisualContentView {

        public Map<String, String> contentInputs = new LinkedHashMap<>() {
                {
                        put("title", "Ingrese el título");
                        put("duration", "Ingrese la duración en minutos");
                        put("genre", "Ingrese el género");
                }
        };

        public Map<String, String> movieInputs = new LinkedHashMap<>() {
                {
                        put("studio", "Ingrese el estudio");
                        put("actors_number", "Ingrese el número de actores");
                }
        };

        public Map<String, String> tvSerieInputs = new LinkedHashMap<>() {
                {
                        put("seasons_number", "Ingrese el número de temporadas");
                }
        };

        public Map<String, String> seasonInputs = new LinkedHashMap<>() {
                {
                        put("season_number", "Ingrese el número de temporada");
                        put("description", "Ingrese la descripción");
                        put("episodes_number", "Ingrese el número de episodios");
                        put("year", "Ingrese el año");
                }
        };

        public Map<String, String> documentaryInputs = new LinkedHashMap<>() {
                {
                        put("topic", "Ingrese el tema");
                }
        };

        public Map<String, String> advertisingSpotInputs = new LinkedHashMap<>() {
                {
                        put("brand", "Ingrese la marca");
                        put("type", "Ingrese el tipo");
                }
        };

        public Map<String, String> streamingInputs = new LinkedHashMap<>() {
                {
                        put("platforms", "Ingrese las plataformas (separadas por comas)");
                        put("is_live", "Ingrese si es en vivo (SI o NO)");
                }
        };

        public void showMainMenu() {
                System.out.println("""
                                1. Peliculas
                                2. Series de TV
                                3. Documentales
                                4. Anuncios Publicitarios
                                5. Transmisiones
                                6. Salir
                                """);
        }

        public void showContentTypeMenu(String contentType) {
                System.out.printf("""
                                1. Listar %s
                                2. Ver detalles de %s
                                3. Agregar %s
                                4. Eliminar %s
                                5. Volver
                                """,
                                contentType, contentType, contentType,
                                contentType, contentType);
        }

        public void listAll(Set<AudiovisualContent> contents, String contentType) {
                System.out.println("Lista de " + contentType + "(" + contents.size() + "):");
                if (contents.isEmpty()) {
                        System.out.println("No hay contenidos disponibles.");
                        return;
                }
                for (AudiovisualContent content : contents) {
                        System.out.println(content.getId() + ". " + content.getTitle());
                }
        }

        public void showDetail(AudiovisualContent content) {
                if (content == null) {
                        System.out.println("El contenido no existe.");
                        return;
                }
                System.out.println("Detalles de " + content.getTitle() + ":");
                content.showDetails();
        }
}
