/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package res.vue;

import res.model.animal.Animal;

import java.awt.*;
import java.util.Objects;

/**
 * @author manuvai.rehua@gmail.com
 */
public class PanelJeu extends javax.swing.JPanel {

    private static final boolean DEBUG = false;
    private Vue fenetre;
    private Graphics cg;

    public PanelJeu() {
    }

    public PanelJeu(Vue fenetre) {
        this.fenetre = fenetre;
    }

    @Override
    public void paintComponent(Graphics g) {
        cg = g;
        super.paintComponent(cg);
        if (Objects.nonNull(fenetre) && Objects.nonNull(cg)) {
            fenetre.dessinerJeu();
        }
    }

    public void drawSouris(int x, int y, int cote) {
        cg.drawImage(RessourcesImages.SOURIS, x, y, cote, cote, this);
    }

    public void drawChat(int x, int y, int cote) {
        cg.drawImage(RessourcesImages.CHAT, x, y, cote, cote, this);
    }

    public void drawCaseIn(int x, int y, int cote) {
        this.drawCaseChemin(x, y, cote);
        cg.drawImage(RessourcesImages.IN, x, y, cote, cote, this);
    }

    public void drawCaseOut(int x, int y, int cote) {
        this.drawCaseChemin(x, y, cote);
        cg.drawImage(RessourcesImages.OUT, x, y, cote, cote, this);
    }

    public void drawCaseMur(int x, int y, int cote) {
        cg.drawImage(RessourcesImages.MUR, x, y, cote, cote, this);
    }

    public void drawCaseChemin(int x, int y, int cote) {
        cg.drawImage(RessourcesImages.CHEMIN, x, y, cote, cote, this);

    }

    public void drawCaseFlecheHaut(int x, int y, int cote) {
        Graphics2D g2d = (Graphics2D) cg;
        this.drawCaseChemin(x, y, cote);
        g2d.rotate(-Math.PI / 2.d, x + cote / 2, y + cote / 2);
        cg.drawImage(RessourcesImages.FLECHE, x, y, cote, cote, this);
        g2d.rotate(Math.PI / 2.d, x + cote / 2, y + cote / 2);

    }


    public void drawCaseFlecheBas(int x, int y, int cote) {
        Graphics2D g2d = (Graphics2D) cg;
        this.drawCaseChemin(x, y, cote);
        g2d.rotate(Math.PI / 2.d, x + cote / 2, y + cote / 2);
        cg.drawImage(RessourcesImages.FLECHE, x, y, cote, cote, this);
        g2d.rotate(-Math.PI / 2.d, x + cote / 2, y + cote / 2);
    }

    public void drawCaseFlecheGauche(int x, int y, int cote) {
        Graphics2D g2d = (Graphics2D) cg;
        this.drawCaseChemin(x, y, cote);
        g2d.rotate(Math.PI, x + cote / 2, y + cote / 2);
        cg.drawImage(RessourcesImages.FLECHE, x, y, cote, cote, this);
        g2d.rotate(Math.PI, x + cote / 2, y + cote / 2);
    }

    public void drawCaseFlecheDroite(int x, int y, int cote) {
        this.drawCaseChemin(x, y, cote);
        cg.drawImage(RessourcesImages.FLECHE, x, y, cote, cote, this);
    }

    public void drawSouris(Animal animal, Rectangle rectangleJeu) {
        Image imageSouris = RessourcesImages.SOURIS;

        int posX = animal.getX() * (int) rectangleJeu.getWidth() + rectangleJeu.x;
        int posY = animal.getY() * (int) rectangleJeu.getWidth() + rectangleJeu.y;

        cg.drawImage(imageSouris,
                posX,
                posY,
                (int) rectangleJeu.getWidth(),
                (int) rectangleJeu.getWidth(),
                this);

        if (DEBUG) {
            String text = animal.toString().substring("res.model.animal.Souris".length());

            Font font = new Font("Arial", Font.BOLD, 18);

            cg.setFont(font);
            cg.setColor(Color.GREEN);
            cg.drawString(text, posX, posY);
        }

    }
}
