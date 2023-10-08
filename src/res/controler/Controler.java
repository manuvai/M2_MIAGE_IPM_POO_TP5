package res.controler;

import res.model.AbstractModel;
import res.model.TypeCase;
import res.model.exceptions.ThreadConcurrentException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controler extends AbstractControler {
    public static final int FRAMES_PER_SECOND = 1;
    public static final long SKIP_TICKS = 1_000 / FRAMES_PER_SECOND;

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
        long sleepPeriod;
        long nextUpdateTick = System.currentTimeMillis();

        model.faireSeDeplacerLesAnimaux();
        model.demandeMiseAjourVue();

        nextUpdateTick += SKIP_TICKS;
        sleepPeriod = nextUpdateTick - System.currentTimeMillis();

        if (sleepPeriod >= 0) {
            try {
                Thread.sleep(sleepPeriod);
            } catch (InterruptedException e) {
                throw new ThreadConcurrentException();
            }
        }
    }

    private TypeCase getNextTypeCase(TypeCase typeCase) {
        List<TypeCase> typeCaseList = Stream.of(TypeCase.FLECHE_HAUT, TypeCase.FLECHE_DROITE,
                        TypeCase.FLECHE_BAS, TypeCase.FLECHE_GAUCHE, TypeCase.CHEMIN)
                .collect(Collectors.toList());

        int nextIndex = !typeCaseList.contains(typeCase)
                ? 0
                : (typeCaseList.indexOf(typeCase) + 1) % typeCaseList.size();

        return typeCaseList.get(nextIndex);
    }
}
