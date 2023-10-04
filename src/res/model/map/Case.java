package res.model.map;

import res.model.TypeCase;

public class Case {

    private int x;

    private int y;

    private TypeCase typeCase;

    public Case(int x, int y, TypeCase typeCase) {
        this.x = x;
        this.y = y;
        this.typeCase = typeCase;
    }

    public TypeCase getTypeCase() {
        return typeCase;
    }

    public void setTypeCase(TypeCase typeCase) {
        this.typeCase = typeCase;
    }
}
