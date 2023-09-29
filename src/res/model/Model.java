package res.model;

import res.model.animal.Animal;
import res.model.animal.Chat;
import res.model.animal.Souris;
import res.model.exceptions.NoEntryFoundException;
import res.model.map.MapLoader;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Model extends AbstractModel {
    
    private static final int NB_SOURIS_IN = 10;

    private List<Animal> animaux = new ArrayList<>();

    private List<Souris> sourisSorties = new ArrayList<>();

    private List<Souris> sourisDansTrou = new ArrayList<>();

    private MapLoader mapLoader = new MapLoader();

    private Map<Integer, Map<Integer, TypeCase>> cases = new HashMap<>();

    public void initialiserAnimaux(String nomFichier) {
        List<Animal> animauxCarte = mapLoader.getCharacters(nomFichier);

        if (Objects.isNull(animauxCarte) || animauxCarte.isEmpty()) {
            return;
        }

        setAnimaux(animauxCarte);
        initialiserSourisDansTrous();
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

        List<Animal> animauxTues = new ArrayList<>();
        List<Souris> souriesSorties = new ArrayList<>();

        for (Animal animal : animaux) {
            TypeCase typeCase = getTypeCase(animal.getX(), animal.getY());

            if (verifierSiMourir(animal)) {
                animauxTues.add(animal);
            }

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

                } else if (TypeCase.OUT.equals(typeCase)) {
                    souriesSorties.add((Souris) animal);
                }
            }

            TypeCase futureCase = getFutureCase(animal);

            if (TypeCase.MUR.equals(futureCase)) {
                inverserSens(animal);
            }

        }

        animauxTues.forEach(this::tuerAnimal);
        souriesSorties.forEach(this::sortirSouris);
    }

    public TypeCase getFutureCase(Animal animal) {
        return Objects.isNull(animal)
                ? null
                : getTypeCase(animal.getX() + animal.getxDir(), animal.getY() + animal.getyDir());
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

    public void faireEntrerNouvelleSouris() {
        if (sourisDansTrou.isEmpty()) {
            return;
        }

        ajouterSourisDansMap(sourisDansTrou.remove(0));
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
        return sourisDansTrou.size();
    }

    @Override
    public int getNbSourisOut() {
        return sourisSorties.size();
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
    private List<Animal> getAnimauxDansCase(int x, int y) {
        return animaux.stream()
                .filter(animal -> animal.getX() == x && animal.getY() == y)
                .collect(Collectors.toList());
    }

    private void tuerAnimal(Animal animal) {

        removeObservateur(animal);

        animaux.remove(animal);
    }

    private boolean verifierSiMourir(Animal inAnimal) {
        Animal animalPlusFort = getAnimalPlusFort(inAnimal.getX(), inAnimal.getY());

        return inAnimal instanceof Souris && animalPlusFort instanceof Chat;
    }

    private void inverserSens(Animal animal) {
        animal.setxDir(-animal.getxDir());
        animal.setyDir(-animal.getyDir());
    }

    private void setAnimaux(List<Animal> inAnimaux) {
        if (Objects.isNull(inAnimaux)) {
            return;
        }

        inAnimaux.forEach(this::addObservateur);

        animaux = inAnimaux;
    }

    private void ajouterSourisDansMap(Souris souris) {
        if (Objects.isNull(souris)) {
            return;
        }

        addObservateur(souris);
        animaux.add(souris);
    }

    private void sortirSouris(Souris souris) {
        int indexSouris = animaux.indexOf(souris);

        if (indexSouris >= 0) {
            removeObservateur(animaux.get(indexSouris));

            sourisSorties.add((Souris) animaux.remove(indexSouris));

        }
    }


    private void initialiserSourisDansTrous() {
        
        Rectangle positionTrou = recupererPositionTrouEntree();

        if (Objects.isNull(positionTrou)) {
            throw new NoEntryFoundException();
        }
        
        for (int i = 0; i < NB_SOURIS_IN; i++) {
            Souris souris = new Souris(positionTrou.x, positionTrou.y);
            souris.setxDir(+1);
            sourisDansTrou.add(souris);
        }
    }

    private Rectangle recupererPositionTrouEntree() {
        Rectangle positionTrou = null;

        for (Map.Entry<Integer, Map<Integer, TypeCase>> entryLigne : cases.entrySet()) {
            int x = entryLigne.getKey();

            for (Map.Entry<Integer, TypeCase> entryColonne : entryLigne.getValue().entrySet()) {
                int y = entryColonne.getKey();

                if (TypeCase.IN.equals(entryColonne.getValue())) {
                    positionTrou = new Rectangle();

                    positionTrou.x = x;
                    positionTrou.y = y;
                }
            }
        }

        return positionTrou;
    }

}
