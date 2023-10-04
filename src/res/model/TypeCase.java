/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package res.model;

import res.model.map.Case;

import java.util.Objects;

/**
 * @author manuvai.rehua@gmail.com
 */
public enum TypeCase {
    MUR,
    IN,
    OUT,
    CHEMIN,
    FLECHE_HAUT,
    FLECHE_DROITE,
    FLECHE_BAS,
    FLECHE_GAUCHE;

    public static boolean isArrow(Case cell) {
        return isArrow(cell.getTypeCase());
    }

    public static boolean isArrow(TypeCase typeCase) {
        return Objects.nonNull(typeCase) && (
                FLECHE_HAUT.equals(typeCase) ||
                FLECHE_DROITE.equals(typeCase) ||
                FLECHE_BAS.equals(typeCase) ||
                FLECHE_GAUCHE.equals(typeCase)
                );

    }
}
