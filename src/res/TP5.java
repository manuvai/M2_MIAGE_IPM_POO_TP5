/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package res;

import res.controler.AbstractControler;
import res.controler.Controler;
import res.model.AbstractModel;
import res.model.Model;
import res.model.TypeCase;
import res.vue.Vue;

/**
 *
 * @author manuvai.rehua@gmail.com
 */
public class TP5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Model model = new Model();

        model.initialiserCarte("assets/map.txt");
        model.initialiserAnimaux("assets/animals.csv");

        AbstractControler controler = new Controler(model);

        Vue v = new Vue(model, controler);
        model.addObservateur(v);
        v.setVisible(true);

        while (!model.partieTerminer()){

            controler.calculerStepSuivant();
            v.dessinerJeu();
        }
    }
    
}
