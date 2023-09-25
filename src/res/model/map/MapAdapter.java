package res.model.map;

import res.model.TypeCase;
import res.model.animal.Animal;
import res.model.animal.Chat;
import res.model.animal.Souris;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapAdapter {

    private final Map<String, TypeCase> correspondingMapTile = new HashMap<>();
    private final Map<String, Class<? extends Animal>> correspondingAnimal = new HashMap<>();

    private static MapAdapter instance;

    protected MapAdapter() {
        correspondingMapTile.put(MapTileTypeString.IN, TypeCase.IN);
        correspondingMapTile.put(MapTileTypeString.OUT, TypeCase.OUT);
        correspondingMapTile.put(MapTileTypeString.MUR, TypeCase.MUR);
        correspondingMapTile.put(MapTileTypeString.CHEMIN, TypeCase.CHEMIN);
        correspondingMapTile.put(MapTileTypeString.FLECHE_HAUT, TypeCase.FLECHE_HAUT);
        correspondingMapTile.put(MapTileTypeString.FLECHE_DROITE, TypeCase.FLECHE_DROITE);
        correspondingMapTile.put(MapTileTypeString.FLECHE_BAS, TypeCase.FLECHE_BAS);
        correspondingMapTile.put(MapTileTypeString.FLECHE_GAUCHE, TypeCase.FLECHE_GAUCHE);

        correspondingAnimal.put(AnimalTypeString.CHAT, Chat.class);
        correspondingAnimal.put(AnimalTypeString.SOURIS, Souris.class);
    }

    public static MapAdapter getInstance() {

        if (Objects.isNull(instance)) {
            instance = new MapAdapter();
        }

        return instance;
    }

    public static Class<? extends Animal> toAnimal(String animalString) {
        return Objects.isNull(animalString)
                ? null
                : getInstance().correspondingAnimal.get(animalString);
    }

    public static TypeCase toMapTile(String tileString) {

        return Objects.isNull(tileString)
                ? null
                : getInstance().correspondingMapTile.get(tileString);
    }

}
