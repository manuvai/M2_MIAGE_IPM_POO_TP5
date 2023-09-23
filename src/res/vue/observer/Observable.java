/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package res.vue.observer;

/**
 * @author david
 */
public interface Observable {
    public abstract void addObservateur(Observateur o);

    public abstract void removeObservateur(Observateur o);

    public abstract void demandeMiseAjourVue();
}
