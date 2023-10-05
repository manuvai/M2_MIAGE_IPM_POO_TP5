package res.model;

import res.model.animal.Animal;
import res.model.animal.Chat;
import res.model.animal.Souris;
import res.model.exceptions.NoEntryFoundException;
import res.model.map.Carte;
import res.model.map.MapLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    /**
     * Cette méthode permet d'initialiser les animaux à partir d'un nom de fichier (notamment les chats)
     *
     * @param nomFichier
     */
    public void initialiserAnimaux(String nomFichier) {
        List<Animal> animauxCarte = mapLoader.getCharacters(nomFichier);

        if (Objects.nonNull(animauxCarte)) {
            setAnimaux(animauxCarte);
            initialiserSourisDansTrous();
        }

    }

    /**
     * Cette méthode permet d'initialiser la carte à partir d'un fichier contenant les cases
     *
     * @param nomFichier
     */
    public void initialiserCarte(String nomFichier) {
        List<List<TypeCase>> casesCarte = mapLoader.getMapTiles(nomFichier);

        for (int i = 0; i < casesCarte.size(); i++) {
            for (int j = 0; j < casesCarte.get(i).size(); j++) {

                TypeCase typeCase = casesCarte.get(i).get(j);

                setTypeCase(j, i, typeCase);

            }
        }
    }

    /**
     * Cette méthode permet de récupérer la future classe d'un animal
     *
     * @param animal
     * @return
     */
    public TypeCase getFutureCase(Animal animal) {
        return Objects.isNull(animal)
                ? null
                : getTypeCase(animal.getX() + animal.getxDir(), animal.getY() + animal.getyDir());
    }

    /**
     * Permet de récupérer l'animal le plus fort d'une liste donnée en paramètre
     *
     * @param animaux
     * @return
     */
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

    /**
     * Permet de récupérer l'animal le plus fort dans une case
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public Animal getAnimalPlusFort(int x, int y) {

        List<Animal> animauxDansCase = getAnimauxDansCase(x, y);

        return animauxDansCase.isEmpty()
                ? null
                : getAnimalPlusFort(animauxDansCase);
    }

    /**
     * Cette méthode permet de déplacer les animaux
     *
     */
    @Override
    public void faireSeDeplacerLesAnimaux() {

        List<Animal> animauxTues = new ArrayList<>();
        List<Souris> souriesSorties = new ArrayList<>();

        for (Animal animal : animaux) {

            if (verifierSiMourir(animal)) {
                animauxTues.add(animal);

            } else {
                TypeCase typeCase = getTypeCase(animal.getX(), animal.getY());
                if (animal instanceof Souris) {
                    calculeDirectionAnimal(animal, typeCase);

                    if (TypeCase.OUT.equals(typeCase)) {
                        souriesSorties.add((Souris) animal);
                    }
                }

                calculerFuturDeplacementAnimal(animal, typeCase);

                if (canMove(animal)) {
                    animal.move();

                }

            }
        }

        calculerSiFaireEntrerSouris();

        animauxTues.forEach(this::tuerAnimal);
        souriesSorties.forEach(this::sortirSouris);
    }

    /**
     * Permet de récupérer le type d'une case
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public TypeCase getTypeCase(int x, int y) {

        return carte.getTypeCase(x, y);
    }

    /**
     * Permet de définir le type d'une case
     *
     * @param x
     * @param y
     * @param tc
     */
    @Override
    public void setTypeCase(int x, int y, TypeCase tc) {
        carte.setTypeCase(x, y, tc);
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

    /**
     * Détermine si un animal peut bouger ou non
     *
     * @param animal
     * @return
     */
    private boolean canMove(Animal animal) {
        boolean canMove = false;

        if (Objects.nonNull(animal)) {
            List<Animal> animauxDansFutureCase = getAnimauxDansCase(animal.getX() + animal.getxDir(),
                    animal.getY() + animal.getyDir());

            TypeCase futureCase = getFutureCase(animal);
            canMove = !TypeCase.MUR.equals(futureCase) &&
                    animauxDansFutureCase.stream()
                            .noneMatch(a -> a.getClass().equals(animal.getClass()));

        }

        return canMove;
    }

    /**
     * Fais faire demi-tour à un animal si nécessaire
     *
     * @param animal
     */
    private void calculerDemiTour(Animal animal) {
        if (Objects.nonNull(animal)) {
            TypeCase futureCase = getFutureCase(animal);

            List<Animal> animauxDansCase = getAnimauxDansCase(
                    animal.getX() + animal.getxDir(),
                    animal.getY() + animal.getyDir());

            boolean isSameInstance = !animauxDansCase.isEmpty() &&
                    animal.getClass()
                            .equals(getAnimalPlusFort(animauxDansCase).getClass());

            if (TypeCase.MUR.equals(futureCase) || isSameInstance) {
                inverserSens(animal);
            }
        }


    }

    /**
     * Fais entrer une nouvelle souris dans la carte
     *
     */
    private void faireEntrerNouvelleSouris() {
        if (!sourisDansTrou.isEmpty()) {
            ajouterSourisDansMap(sourisDansTrou.remove(0));
        }

    }

    /**
     * Effectue les calculs pour déterminer la future direction d'un animal
     *
     * @param animal
     * @param typeCase
     */
    private void calculeDirectionAnimal(Animal animal, TypeCase typeCase) {
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

    /**
     * Détermine le prochain déplacement de l'animal
     *
     * @param animal
     * @param typeCase
     */
    private void calculerFuturDeplacementAnimal(Animal animal, TypeCase typeCase) {
        if (Objects.nonNull(animal) &&
                Objects.nonNull(typeCase) &&
                !TypeCase.isArrow(typeCase)
        ) {
            calculerDemiTour(animal);

        }
    }

    /**
     * Récupère la liste des animaux dans une case donnée
     *
     * @param x
     * @param y
     * @return
     */
    private List<Animal> getAnimauxDansCase(int x, int y) {
        return animaux.stream()
                .filter(animal -> animal.getX() == x && animal.getY() == y)
                .collect(Collectors.toList());
    }

    /**
     * Tue un animal
     *
     * @param animal
     */
    private void tuerAnimal(Animal animal) {

        animaux.remove(animal);
    }

    /**
     * Récupère le nombre de souris sur le plateau
     *
     * @return
     */
    private int recupererNbSourisBougeant() {
        return (int) animaux.stream()
                .filter(Souris.class::isInstance)
                .count();
    }

    /**
     * Détermine si un animal doit mourir
     *
     * @param inAnimal
     * @return
     */
    private boolean verifierSiMourir(Animal inAnimal) {
        Animal animalPlusFort = getAnimalPlusFort(inAnimal.getX(), inAnimal.getY());

        return inAnimal instanceof Souris && animalPlusFort instanceof Chat;
    }

    /**
     * Rajoute une souris si nécessaire
     *
     */
    private void calculerSiFaireEntrerSouris() {

        boolean isTimeToEnter = Objects.isNull(dateDerniereEntree) ||
                (new Date()).getTime() - dateDerniereEntree.getTime() >= DELAY_TO_ENTER * 1000;

        if (isTimeToEnter && isPossibleToEnter()) {
            faireEntrerNouvelleSouris();
            dateDerniereEntree = new Date();
        }
    }

    /**
     * Détermine si une souris peut entrer
     *
     * @return
     */
    private boolean isPossibleToEnter() {
        Rectangle positionTrou = recupererPositionTrouEntree();

        List<Animal> animauxSurCaseTrou = getAnimauxDansCase((int) positionTrou.getX(), (int) positionTrou.getY());

        return animauxSurCaseTrou.isEmpty();
    }

    /**
     * Inverse le sens de déplacement d'un animal donné
     *
     * @param animal
     */
    private void inverserSens(Animal animal) {
        animal.setxDir(-animal.getxDir());
        animal.setyDir(-animal.getyDir());
    }

    /**
     * Affecte les animaux
     *
     * @param inAnimaux
     */
    private void setAnimaux(List<Animal> inAnimaux) {
        if (Objects.nonNull(inAnimaux)) {
            animaux = inAnimaux;
        }

    }

    /**
     * Ajoute une souris dans le plateau
     *
     * @param souris
     */
    private void ajouterSourisDansMap(Souris souris) {
        if (Objects.nonNull(souris)) {
            animaux.add(souris);

        }

    }

    /**
     * Sors une souris du plateau
     *
     * @param souris
     */
    private void sortirSouris(Souris souris) {
        int indexSouris = animaux.indexOf(souris);

        if (indexSouris >= 0) {

            sourisSorties.add(souris);
            animaux.remove(indexSouris);

        }
    }

    /**
     * Initialise les souris dans le trou
     *
     */
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

    /**
     * Récupère la position du trou d'entrée
     *
     * @return
     */
    private Rectangle recupererPositionTrouEntree() {
        return carte.recupererPositionTrouEntree();
    }

}
