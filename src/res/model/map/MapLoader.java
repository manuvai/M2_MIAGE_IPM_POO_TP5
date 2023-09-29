package res.model.map;

import res.model.TypeCase;
import res.model.animal.Animal;
import res.model.exceptions.AnimalCreationFailedException;
import res.model.exceptions.ImpossibleToExtractLinesException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapLoader {

    public static final String SPLIT_STRING = ";";

    public List<List<TypeCase>> getMapTiles(String file) {

        List<String> mapStrings = extractMapString(file);

        List<List<String>> tilesString = extractTileString(mapStrings);

        return constructTilesFromString(tilesString);

    }
    public List<Animal> getCharacters(String file) {

        List<String> mapStrings = extractMapString(file);

        List<List<String>> records = extractTileString(mapStrings, SPLIT_STRING);

        List<Animal> animaux = new ArrayList<>();

        for (int i = 1; i < records.size(); i++) {
            Class<? extends Animal> animalClazz = MapAdapter.toAnimal(records.get(i).get(0));

            if (Objects.nonNull(animalClazz)) {
                try {
                    Constructor<?> constructor = animalClazz.getConstructor(int.class, int.class);
                    int x = Integer.parseInt(records.get(i).get(1));
                    int y = Integer.parseInt(records.get(i).get(2));

                    Animal animal = (Animal) constructor.newInstance(x, y);

                    animal.setxDir(Integer.parseInt(records.get(i).get(3)));
                    animal.setyDir(Integer.parseInt(records.get(i).get(4)));

                    animaux.add(animal);

                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new AnimalCreationFailedException(animalClazz);
                }
            }
        }

        return animaux;

    }

    private List<String> extractMapString(String file) {
        InputStream inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(file);
        List<String> mapStrings;

        try {
            mapStrings = readFromInputStream(inputStream);
        } catch (IOException e) {
            throw new ImpossibleToExtractLinesException();
        }
        return mapStrings;
    }

    private List<List<String>> extractTileString(List<String> mapStrings) {
        return extractTileString(mapStrings, "");
    }

    private List<List<String>> extractTileString(List<String> mapStrings, String splitString) {
        return mapStrings.stream()
                .map(line -> Arrays.asList(line.split(splitString)))
                .collect(Collectors.toList());
    }

    /**
     * Retourne une liste de liste de cases à partir des lignes d'un fichier.
     *
     * @param tilesString
     * @return
     */
    private List<List<TypeCase>> constructTilesFromString(
            List<List<String>> tilesString
    ) {
        List<List<TypeCase>> mapTiles = new ArrayList<>();

        tilesString.forEach(line -> {
            List<TypeCase> tempTiles = new ArrayList<>();
            line.forEach(element -> {
                TypeCase mapTile = MapAdapter.toMapTile(element);
                tempTiles.add(mapTile);
            });
            mapTiles.add(tempTiles);
        });

        return mapTiles;
    }

    /**
     * Permet de lire le contenu d'un fichier contenu dans le InputStream
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private List<String> readFromInputStream(InputStream inputStream)
            throws IOException {

        List<String> responseLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream)
        )) {
            String line;
            while ((line = br.readLine()) != null) {
                responseLines.add(line);
            }
        }
        return responseLines;
    }
}
