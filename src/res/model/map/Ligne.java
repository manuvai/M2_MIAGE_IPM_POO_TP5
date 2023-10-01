package res.model.map;

import res.model.TypeCase;

import java.util.*;

public class Ligne {
    private Map<Integer, TypeCase> cases = new HashMap<>();

    public void put(Integer index, TypeCase typeCase) {
        cases.put(index, typeCase);
    }

    public TypeCase get(Integer index) {
        return cases.get(index);
    }

    public int size() {
        return cases.size();
    }

    public Collection<TypeCase> values() {
        return cases.values();
    }

    public Set<Map.Entry<Integer, TypeCase>> entrySet() {
        return cases.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ligne ligne)) return false;
        return Objects.equals(cases, ligne.cases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cases);
    }
}
