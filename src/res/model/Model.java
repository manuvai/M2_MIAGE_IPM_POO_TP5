package res.model;

import res.model.animal.Animal;
import res.model.animal.Chat;
import res.model.animal.Souris;
import res.model.map.MapLoader;

import java.util.*;
import java.util.stream.Collectors;

public class Model extends AbstractModel {

    private List<Animal> animaux = new ArrayList<>();

    private MapLoader mapLoader = new MapLoader();

    private Map<Integer, Map<Integer, TypeCase>> cases = new HashMap<>();

    public void initialiserAnimaux(String nomFichier) {
        List<Animal> animauxCarte = mapLoader.getCharacters(nomFichier);

        if (Objects.isNull(animauxCarte) || animauxCarte.isEmpty()) {
            return;
        }

        animaux = animauxCarte;
    }

    public void initialiserCarte(String nomFichier) {
        List<List<TypeCase>> casesCarte = mapLoader.getMapTiles(nomFichier);

        for (int i = 0; i < casesCarte.size(); i++) {
            for (int j = 0; j < casesCarte.get(i).size(); j++) {

                TypeCase typeCase = casesCarte.get(i).get(j);

                setTypeCase(j, i, typeCase);

            }
        }
    }

    @Override
    public void faireSeDeplacerLesAnimaux() {
        for (Animal animal : animaux) {
            TypeCase typeCase = getTypeCase(animal.getX(), animal.getY());

            if (animal instanceof Souris) {
                if (TypeCase.FLECHE_HAUT.equals(typeCase)) {
                    animal.setxDir(0);
                    animal.setyDir(-1);

                }
                else if (TypeCase.FLECHE_DROITE.equals(typeCase)) {
                    animal.setxDir(+1);
                    animal.setyDir(0);

                }
                else if (TypeCase.FLECHE_BAS.equals(typeCase)) {
                    animal.setxDir(0);
                    animal.setyDir(+1);

                }
                else if (TypeCase.FLECHE_GAUCHE.equals(typeCase)) {
                    animal.setxDir(-1);
                    animal.setyDir(0);

                }
            }

            TypeCase futureCase = getFutureCase(animal);

            if (TypeCase.MUR.equals(futureCase)) {
                inverserSens(animal);
            }

            animal.setX(animal.getX() + animal.getxDir());
            animal.setY(animal.getY() + animal.getyDir());

        }
    }

    public TypeCase getFutureCase(Animal animal) {
        return Objects.isNull(animal)
                ? null
                : getTypeCase(animal.getX() + animal.getxDir(), animal.getY() + animal.getyDir());
    }

    private void inverserSens(Animal animal) {
        animal.setxDir(-animal.getxDir());
        animal.setyDir(-animal.getyDir());
    }

    @Override
    public TypeCase getTypeCase(int x, int y) {

        Map<Integer, TypeCase> ligneCase = cases.get(x);

        return Objects.isNull(ligneCase)
                ? null
                : ligneCase.get(y);
    }

    @Override
    public void setTypeCase(int x, int y, TypeCase tc) {
        cases.computeIfAbsent(x, k -> new HashMap<>())
                .put(y, tc);
    }

    @Override
    public Animal getAnimalPlusFort(int x, int y) {

        List<Animal> animauxDansCase = getAnimauxDansCase(x, y);

        return animauxDansCase.isEmpty()
                ? null
                : getAnimalPlusFort(animauxDansCase);
    }

    public Animal getAnimalPlusFort(List<Animal> animaux) {
        int indexBest = -1;

        if (Objects.nonNull(animaux)) {
            for (int i = 0; i < animaux.size(); i++) {
                if (indexBest == -1 || animaux.get(i) instanceof Chat) {
                    indexBest = i;
                }
            }
        }

        return Objects.isNull(animaux)
                ? null
                : animaux.get(indexBest);
    }

    private List<Animal> getAnimauxDansCase(int x, int y) {
        return animaux.stream()
                .filter(animal -> animal.getX() == x && animal.getY() == y)
                .collect(Collectors.toList());
    }

    @Override
    public int getLargeur() {
        Map<Integer, TypeCase> premiereLigne = cases.get(0);

        return Objects.isNull(premiereLigne)
                ? 0
                : premiereLigne.size();
    }

    @Override
    public int getHauteur() {
        return cases.size();
    }

    @Override
    public int getNbSourisIn() {
        return 0;
    }

    @Override
    public int getNbSourisOut() {
        return 0;
    }

    @Override
    public int getNbFlecheUtilisee() {

        int count = 0;

        for (Map<Integer, TypeCase> ligne : cases.values()) {
            count += ligne.values()
                    .stream()
                    .filter(TypeCase::isArrow)
                    .count();
        }

        return count;
    }

    @Override
    public int getNbFlecheMax() {
        // TODO Déterminer un nombre de flèches maximal

        return 2;
    }

    @Override
    public boolean partieTerminer() {
        return false;
    }
}
