package org.aussiebox.wingcrafter.spells.util;

import org.aussiebox.wingcrafter.spells.FlamethrowerSpell;
import org.aussiebox.wingcrafter.spells.FrostbeamSpell;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpellRegistry {
    private static final Map<String, Class<? extends Spell>> registry = new LinkedHashMap<>();

    static {
        register("frostbeam", FrostbeamSpell.class);
        register("flamethrower", FlamethrowerSpell.class);
        register("test1", FrostbeamSpell.class);
        register("test2", FrostbeamSpell.class);
        register("test3", FrostbeamSpell.class);
        register("test4", FrostbeamSpell.class);
    }

    public static void register(String id, Class<? extends Spell> spellClass) {
        registry.put(id, spellClass);
    }

    public static Spell getSpell(String id) throws Exception {
        Class<? extends Spell> spellClass = registry.get(id);
        return (spellClass != null) ? spellClass.getDeclaredConstructor().newInstance() : null;
    }

    public static Map<String, Spell> getSpellMap() throws Exception {
        Map<String, Spell> spellMap = new HashMap<>();
        for (String spellID : registry.keySet()) {
            Class<? extends Spell> spellClass = registry.get(spellID);
            if (spellClass != null)
                spellMap.put(spellID, spellClass.getDeclaredConstructor().newInstance());
        }
        return spellMap;
    }
}
