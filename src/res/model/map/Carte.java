package res.model.map;

import res.model.TypeCase;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Carte {

    private Map<Integer, Ligne> cases = new HashMap<>();

    public void setTypeCase(int x, int y, TypeCase tc) {
        cases.computeIfAbsent(x, k -> new Ligne())
                .put(y, tc);
    }

    public TypeCase getTypeCase(int x, int y) {
        Ligne ligneCase = cases.get(x);

        return Objects.isNull(ligneCase)
                ? null
                : ligneCase.get(y);
    }

    public int getLargeur() {
        Ligne premiereLigne = cases.get(0);

        return Objects.isNull(premiereLigne)
                ? 0
                : premiereLigne.size();
    }

    public int getHauteur() {
        return cases.size();
    }

    public int getNbFlecheUtilisee() {
        int count = 0;

        for (Ligne ligne : cases.values()) {
            count += ligne.values()
                    .stream()
                    .filter(TypeCase::isArrow)
                    .count();
        }

        return count;
    }

    public Rectangle recupererPositionTrouEntree() {
        Rectangle positionTrou = null;

        for (Map.Entry<Integer, Ligne> entryLigne : cases.entrySet()) {
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
