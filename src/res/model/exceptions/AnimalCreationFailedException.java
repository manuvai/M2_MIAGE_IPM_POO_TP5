package res.model.exceptions;

import res.model.animal.Animal;

public class AnimalCreationFailedException extends NoStackTraceRuntimeException {
    public AnimalCreationFailedException(Class<? extends Animal> classOfAnimal) {
        super(classOfAnimal.getSimpleName() + " creation failed");
    }
}
