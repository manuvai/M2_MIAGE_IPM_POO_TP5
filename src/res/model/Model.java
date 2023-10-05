package res.model;

import res.model.animal.Animal;
import res.model.animal.Chat;
import res.model.animal.Souris;
import res.model.exceptions.NoEntryFoundException;
import res.model.map.Carte;
import res.model.map.Case;
import res.model.map.MapLoader;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Model extends AbstractModel {
    
    private static final int NB_SOURIS_IN = 4;

    private static final int DELAY_TO_ENTER = 5;

    private List<Animal> animaux = new ArrayList<>();

    private List<Souris> sourisSorties = new ArrayList<>();

    private List<Souris> sourisDansTrou = new ArrayList<>();

    private MapLoader mapLoader = new MapLoader();

    private Carte carte = new Carte();

    private Date dateDerniereEntree;

    public void initialiserAnimaux(String nomFichier) {
        List<Animal> animauxCarte = mapLoader.getCharacters(nomFichier);

        if (Objects.nonNull(animauxCarte)) {
            setAnimaux(animauxCarte);
            initialiserSourisDansTrous();
        }

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
                calculerDeplacementAnimal(animal, typeCase);

                if (TypeCase.OUT.equals(typeCase)) {
                    souriesSorties.add((Souris) animal);
                }
            }

            if (!TypeCase.isArrow(typeCase)) {
                calculerDemiTour(animal);

            }

            TypeCase futureCase = getFutureCase(animal);

            if (TypeCase.MUR.equals(futureCase)) {
                animal.setxDir(0);
                animal.setyDir(0);
            }

            if (canMove(animal)) {
                animal.move();

            }
        }

        calculerSiFaireEntrerSouris();

        animauxTues.forEach(this::tuerAnimal);
        souriesSorties.forEach(this::sortirSouris);
    }

    private boolean canMove(Animal animal) {
        List<Animal> animauxDansFutureCase = getAnimauxDansCase(animal.getX() + animal.getxDir(), animal.getY() + animal.getyDir());

        return animauxDansFutureCase.isEmpty();
    }

    public TypeCase getFutureCase(Animal animal) {
        return Objects.isNull(animal)
                ? null
                : getTypeCase(animal.getX() + animal.getxDir(), animal.getY() + animal.getyDir());
    }

    @Override
    public TypeCase getTypeCase(int x, int y) {

        return carte.getTypeCase(x, y);
    }

    @Override
    public void setTypeCase(int x, int y, TypeCase tc) {
        carte.setTypeCase(x, y, tc);
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
        if (!sourisDansTrou.isEmpty()) {
            ajouterSourisDansMap(sourisDansTrou.remove(0));
        }

    }

    @Override
    public int getLargeur() {
        return carte.getLargeur();
    }

    @Override
    public int getHauteur() {
        return carte.getHauteur();
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

        return carte.getNbFlecheUtilisee();
    }

    @Override
    public int getNbFlecheMax() {
        // TODO Déterminer un nombre de flèches maximal

        return 2;
    }

    @Override
    public boolean partieTerminer() {
        return getNbSourisIn() == 0 && recupererNbSourisBougeant() == 0;
    }


    private void calculerDemiTour(Animal animal) {
        if (Objects.nonNull(animal)) {
            TypeCase futureCase = getFutureCase(animal);

            List<Animal> animauxDansCase = getAnimauxDansCase(
                    animal.getX() + animal.getxDir(),
                    animal.getY() + animal.getyDir());

            boolean isSameInstance = !animauxDansCase.isEmpty() &&
                    animal.getClass()
                            .equals(getAnimalPlusFort(animauxDansCase).getClass());

            // TODO Corriger bug

            if (TypeCase.MUR.equals(futureCase) || isSameInstance) {
                inverserSens(animal);
            }
        }


    }

    private void calculerDeplacementAnimal(Animal animal, TypeCase typeCase) {
        if (Objects.nonNull(animal) && Objects.nonNull(typeCase)) {

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
    }
    private List<Animal> getAnimauxDansCase(int x, int y) {
        return animaux.stream()
                .filter(animal -> animal.getX() == x && animal.getY() == y)
                .collect(Collectors.toList());
    }

    private void tuerAnimal(Animal animal) {

        animaux.remove(animal);
    }

    private int recupererNbSourisBougeant() {
        return (int) animaux.stream()
                .filter(Souris.class::isInstance)
                .count();
    }

    private boolean verifierSiMourir(Animal inAnimal) {
        Animal animalPlusFort = getAnimalPlusFort(inAnimal.getX(), inAnimal.getY());

        return inAnimal instanceof Souris && animalPlusFort instanceof Chat;
    }

    private void calculerSiFaireEntrerSouris() {

        boolean isTimeToEnter = Objects.isNull(dateDerniereEntree) ||
                (new Date()).getTime() - dateDerniereEntree.getTime() >= DELAY_TO_ENTER * 1000;

        if (isTimeToEnter && isPossibleToEnter()) {
            faireEntrerNouvelleSouris();
            dateDerniereEntree = new Date();
        }
    }

    private boolean isPossibleToEnter() {
        Rectangle positionTrou = recupererPositionTrouEntree();

        List<Animal> animauxSurCaseTrou = getAnimauxDansCase((int) positionTrou.getX(), (int) positionTrou.getY());

        return animauxSurCaseTrou.isEmpty();
    }

    private void inverserSens(Animal animal) {
        animal.setxDir(-animal.getxDir());
        animal.setyDir(-animal.getyDir());
    }

    private void setAnimaux(List<Animal> inAnimaux) {
        if (Objects.nonNull(inAnimaux)) {
            animaux = inAnimaux;
        }

    }

    private void ajouterSourisDansMap(Souris souris) {
        if (Objects.nonNull(souris)) {
            animaux.add(souris);

        }

    }

    private void sortirSouris(Souris souris) {
        int indexSouris = animaux.indexOf(souris);

        if (indexSouris >= 0) {

            sourisSorties.add(souris);
            animaux.remove(indexSouris);

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
        return carte.recupererPositionTrouEntree();
    }

}
