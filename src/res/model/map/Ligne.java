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
}
