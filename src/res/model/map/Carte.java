package res.model.map;

import res.model.TypeCase;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Carte {

    private Map<Integer, Ligne> lignes = new HashMap<>();

    public void setTypeCase(int x, int y, TypeCase tc) {
        lignes.computeIfAbsent(x, k -> new Ligne())
                .setTypeCase(x, y, tc);
    }

    public TypeCase getTypeCase(int x, int y) {
        Ligne ligneCase = lignes.get(x);

        return Objects.isNull(ligneCase)
                ? null
                : ligneCase.get(y).getTypeCase();
    }

    public int getLargeur() {
        Ligne premiereLigne = lignes.get(0);

        return Objects.isNull(premiereLigne)
                ? 0
                : premiereLigne.size();
    }

    public int getHauteur() {
        return lignes.size();
    }

    public int getNbFlecheUtilisee() {
        int count = 0;

        for (Ligne ligne : lignes.values()) {
            count += ligne.values()
                    .stream()
                    .filter(TypeCase::isArrow)
                    .count();
        }

        return count;
    }

    public Rectangle recupererPositionTrouEntree() {
        Rectangle positionTrou = null;

        for (Map.Entry<Integer, Ligne> entryLigne : lignes.entrySet()) {
            int x = entryLigne.getKey();

            for (Map.Entry<Integer, Case> entryColonne : entryLigne.getValue().entrySet()) {
                int y = entryColonne.getKey();

                if (TypeCase.IN.equals(entryColonne.getValue().getTypeCase())) {
                    positionTrou = new Rectangle();

                    positionTrou.x = x;
                    positionTrou.y = y;
                }
            }
        }

        return positionTrou;
    }
}
