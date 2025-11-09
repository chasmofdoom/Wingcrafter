package org.aussiebox.wingcrafter.spells;

import net.minecraft.server.network.ServerPlayerEntity;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.attach.ModAttachmentTypes;
import org.aussiebox.wingcrafter.attach.ModCustomAttachedData;

import java.util.Objects;

public class Spells {
    static String[] spells = {
            "frostbeam",
            "skyShield"
    };
    static Integer[] soulTaken = {
            3,
            2
    };

    public static void cast(String spellID, ServerPlayerEntity player) {
        Wingcrafter.LOGGER.info("Player {} cast spell {}", player.getStringifiedName(), spellID);
        int i = 0;
        for (String spell : spells) {
            if (Objects.equals(spell, spellID)) {
                ModCustomAttachedData data = player.getAttachedOrSet(ModAttachmentTypes.SOUL_ATTACH, ModCustomAttachedData.DEFAULT);
                player.setAttached(ModAttachmentTypes.SOUL_ATTACH, data.removeSoul(data, soulTaken[i]));
            }
            i++;
        }
    }
}
