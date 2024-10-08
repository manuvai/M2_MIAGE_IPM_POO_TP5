/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package res.vue;

import res.controler.AbstractControler;
import res.model.AbstractModel;
import res.model.TypeCase;
import res.model.animal.Animal;
import res.model.animal.Chat;
import res.model.animal.Souris;
import res.vue.observer.Observateur;

import java.awt.*;
import java.util.Objects;

/**
 * @author manuvai.rehua@gmail.com
 */
public class Vue extends javax.swing.JFrame implements Observateur {

    private AbstractModel model;
    private MouseListener mouseListener;
    private AbstractControler controler;

    /**
     * Creates new form
     *
     * @param inModel
     * @param inControler
     */
    public Vue(AbstractModel inModel, AbstractControler inControler) {
        model = inModel;
        controler = inControler;
        initComponents();
        mouseListener = new MouseListener(controler);
        panelJeu.addMouseListener(this.mouseListener);
    }

    public void dessinerJeu() {
        labelNbSourisIN.setText("Nombre souris cachées : " + model.getNbSourisIn());
        labelNbSourisOut.setText("Nombre souris sauvées : " + model.getNbSourisOut());
        labelNbFleches.setText("Flèches : " + model.getNbFlecheUtilisee() + "/" + model.getNbFlecheMax());

        Rectangle rectangle = initialiserRectangleJeu();

        int x = (int) rectangle.getX();
        int y = (int) rectangle.getY();
        int cote = (int) rectangle.getHeight();

        mouseListener.updateDimension(
                x,
                y,
                x + cote * model.getHauteur(),
                y + cote * model.getLargeur(),
                cote);

        for (int xP = 0; xP < model.getHauteur(); xP++) {
            for (int yP = 0; yP < model.getLargeur(); yP++) {
                int xCase = x + xP * cote;
                int yCase = y + yP * cote;
                TypeCase typeCase = model.getTypeCase(xP, yP);

                if (Objects.nonNull(typeCase)) {
                    switch (typeCase) {
                        case IN:
                            panelJeu.drawCaseIn(xCase, yCase, cote);
                            break;

                        case OUT:
                            panelJeu.drawCaseOut(xCase, yCase, cote);
                            break;

                        case MUR:
                            panelJeu.drawCaseMur(xCase, yCase, cote);
                            break;

                        case CHEMIN:
                            panelJeu.drawCaseChemin(xCase, yCase, cote);
                            break;

                        case FLECHE_HAUT:
                            panelJeu.drawCaseFlecheHaut(xCase, yCase, cote);
                            break;

                        case FLECHE_DROITE:
                            panelJeu.drawCaseFlecheDroite(xCase, yCase, cote);
                            break;

                        case FLECHE_BAS:
                            panelJeu.drawCaseFlecheBas(xCase, yCase, cote);
                            break;

                        case FLECHE_GAUCHE:
                            panelJeu.drawCaseFlecheGauche(xCase, yCase, cote);
                            break;

                    }

                }

                Animal animalPlusFort = model.getAnimalPlusFort(xP, yP);

                if (animalPlusFort instanceof Souris) {
                    panelJeu.drawSouris(animalPlusFort, rectangle);

                } else if (animalPlusFort instanceof Chat) {
                    panelJeu.drawChat(xCase, yCase, cote);

                }

            }
        }
    }

    private Rectangle initialiserRectangleJeu() {
        int x;
        int y;
        int cote;

        int h = panelJeu.getWidth();
        int l = panelJeu.getHeight();

        if (l / model.getLargeur() > h / model.getHauteur()) {
            cote = h / model.getHauteur();
            x = 0;
            y = (int) ((l - (cote * model.getLargeur())) / 2.f);

        } else {
            cote = l / model.getLargeur();
            x = (int) ((h - (cote * model.getHauteur())) / 2.f);
            y = 0;

        }

        return new Rectangle(x, y, cote, cote);
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelNbSourisIN = new javax.swing.JLabel();
        labelNbFleches = new javax.swing.JLabel();
        labelNbSourisOut = new javax.swing.JLabel();
        panelJeu = new res.vue.PanelJeu(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelNbSourisIN.setText("jLabel1");

        labelNbFleches.setText("nbFleche");

        labelNbSourisOut.setText("jLabel3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(labelNbSourisIN, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelNbSourisOut, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelNbFleches, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(panelJeu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(panelJeu, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelNbSourisIN)
                                        .addComponent(labelNbFleches)
                                        .addComponent(labelNbSourisOut)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelNbFleches;
    private javax.swing.JLabel labelNbSourisIN;
    private javax.swing.JLabel labelNbSourisOut;
    private res.vue.PanelJeu panelJeu;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update() {
        repaint();
    }
}
