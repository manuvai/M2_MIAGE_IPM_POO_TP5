package res.controler;

import res.model.AbstractModel;

public class Controler extends AbstractControler {
    public Controler(AbstractModel model) {
        super(model);
    }

    @Override
    public void cliqueSur(int x, int y) {

    }

    @Override
    public void calculerStepSuivant() {
        // TODO Essayer de mettre des sleep pour éviter le rafraîchissement trop rapide
        
        model.faireSeDeplacerLesAnimaux();
    }
}
