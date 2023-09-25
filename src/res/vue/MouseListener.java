/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package res.vue;

/**
 * @author manuvai.rehua@gmail.com
 */

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import res.controler.AbstractControler;


public class MouseListener extends MouseAdapter {
    int x0;
    int y0;
    int x1;
    int y1;
    int cote;
    int xDepart;
    int yDepart;
    int xArrive;
    int yArrive;
    boolean deuxiemeClic = false;
    private AbstractControler controler;

    public MouseListener(AbstractControler controler) {
        super();
        this.controler = controler;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int xSouris = e.getX();
        int ySouris = e.getY();

        if (alinterieur(xSouris, ySouris)) {
            int i = numeroCaseColonne(xSouris);
            int j = numeroCaseLigne(ySouris);
            this.controler.cliqueSur(i, j);
        }
    }

    public void updateDimension(int x0, int y0, int x1, int y1, int cote) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.cote = cote;
    }

    /**
     * Cette m�thode v�rifie que le clic � la position xPos, et yPos
     * est bien dans la zone de jeu.
     *
     * @param xPos : la position X au moment du clic
     * @param yPos : la position Y au moment du clic
     * @return <b>true</b> si la position est correct, <b>false</b> sinon.
     * @see #alinterieur(int xPos, int yPos)
     * @since 1.0
     */
    private boolean alinterieur(int xPos, int yPos) {
        return ((x0 < xPos) && (xPos < x1) && (y0 < yPos) && (yPos < y1));
    }

    /**
     * Cette m�thode renvoie la colonne par rapport � la position X du clic
     *
     * @param xPos : la position X au moment du clic
     * @return Le num�ro de la colonne
     * @see #numeroCaseColonne(int xPos)
     * @since 1.0
     */
    private int numeroCaseColonne(int xPos) {
        return (xPos - x0) / cote;
    }

    /**
     * Cette m�thode renvoie la rang�e par rapport � la position Y du clic
     *
     * @param yPos : la position Y au moment du clic
     * @return Le num�ro de la rang�e
     * @see #numeroCaseLigne(int YPos)
     * @since 1.0
     */
    private int numeroCaseLigne(int yPos) {
        return (yPos - y0) / cote;
    }
}
