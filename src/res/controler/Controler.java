package res.controler;

import res.model.AbstractModel;
import res.model.TypeCase;
import res.model.exceptions.ThreadConcurrentException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controler extends AbstractControler {
    private static final long PERIOD = 512_000_000L; //ms -> nano // sleeping time
    public Controler(AbstractModel model) {
        super(model);
    }

    @Override
    public void cliqueSur(int x, int y) {

        TypeCase typeCase = model.getTypeCase(x, y);

        boolean isChemin = TypeCase.CHEMIN.equals(typeCase);

        if (!isChemin ||
                model.getNbFlecheUtilisee() < model.getNbFlecheMax()
        ) {
            if (isChemin || TypeCase.isArrow(typeCase)) {

                TypeCase nextTypeCase = getNextTypeCase(typeCase);

                model.setTypeCase(x, y, nextTypeCase);
            }
        }

    }

    @Override
    public void calculerStepSuivant() {
        long beforeTime;
        long afterTime;
        long diff;
        long sleepTime;
        long overSleepTime = 0;

        beforeTime =  System.nanoTime();

        model.faireSeDeplacerLesAnimaux();
        model.demandeMiseAjourVue();

        afterTime = System.nanoTime();
        diff = afterTime - beforeTime;
        sleepTime = (PERIOD - diff) - overSleepTime;

        // If the sleep time is between 0 and the period, we can happily sleep
        if ( sleepTime < PERIOD && sleepTime > 0){
            try {
                Thread.sleep(sleepTime / 1_000_000L);
            } catch (Exception ex) {
                throw new ThreadConcurrentException();
            }
        }
    }

    private static TypeCase getNextTypeCase(TypeCase typeCase) {
        List<TypeCase> typeCaseList = Stream.of(TypeCase.FLECHE_HAUT, TypeCase.FLECHE_DROITE,
                        TypeCase.FLECHE_BAS, TypeCase.FLECHE_GAUCHE, TypeCase.CHEMIN)
                .collect(Collectors.toList());

        int nextIndex = !typeCaseList.contains(typeCase)
                ? 0
                : (typeCaseList.indexOf(typeCase) + 1) % typeCaseList.size();

        return typeCaseList.get(nextIndex);
    }
}
