package res.model.map;

import res.model.TypeCase;

import java.util.*;

public class Ligne {
    private Map<Integer, Case> cases = new HashMap<>();

    public Case get(Integer index) {
        return cases.get(index);
    }

    public int size() {
        return cases.size();
    }

    public Collection<Case> values() {
        return cases.values();
    }

    public Set<Map.Entry<Integer, Case>> entrySet() {
        return cases.entrySet();
    }

    public void setTypeCase(int x, int y, TypeCase typeCase) {
        cases.computeIfAbsent(y, k -> new Case(x, y, typeCase))
                .setTypeCase(typeCase);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ligne)) return false;
        Ligne that = (Ligne) o;
        return Objects.equals(cases, that.cases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cases);
    }
}
