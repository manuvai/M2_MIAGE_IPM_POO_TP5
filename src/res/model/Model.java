package res.model;

import res.model.animal.Animal;

import java.awt.*;

public class Model extends AbstractModel {
    @Override
    public void faireSeDeplacerLesAnimaux() {

    }

    @Override
    public TypeCase getTypeCase(int x, int y) {
        return null;
    }

    @Override
    public void setTypeCase(int x, int y, TypeCase tc) {

    }

    @Override
    public Animal getAnimalPlusFort(int x, int y) {
        return null;
    }

    @Override
    public int getLargeur() {
        return (int) Toolkit.getDefaultToolkit()
                .getScreenSize()
                .getWidth();
    }

    @Override
    public int getHauteur() {
        return (int) Toolkit.getDefaultToolkit()
                .getScreenSize()
                .getHeight();
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
        return 0;
    }

    @Override
    public int getNbFlecheMax() {
        return 0;
    }

    @Override
    public boolean partieTerminer() {
        return false;
    }
}
