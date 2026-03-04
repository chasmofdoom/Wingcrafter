package org.aussiebox.wingcrafter.util;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.aussiebox.wingcrafter.Wingcrafter;

public class WingcrafterUtil {

    public static void grantAdvancement(ServerPlayerEntity player, String path) {
        AdvancementEntry advancement = player.getEntityWorld().getServer().getAdvancementLoader().get(Identifier.of(Wingcrafter.MOD_ID, path));
        PlayerAdvancementTracker advancementTracker = player.getAdvancementTracker();
        if (!advancementTracker.getProgress(advancement).isDone()) {
            for (String missing : advancementTracker.getProgress(advancement).getUnobtainedCriteria()) {
                advancementTracker.grantCriterion(advancement, missing);
            }
        }
    }

    public static Vec3d shiftVecTowardsVec(Vec3d point, Vec3d shiftTowards, double amount) {
        return point.add(shiftTowards.subtract(point).normalize().multiply(amount));
    }

}
