package org.aussiebox.wingcrafter.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.aussiebox.wingcrafter.network.SoulScrollDataPayload;

public class SoulScrollSpellSelectScreenHandler extends ScreenHandler {
    public String spell1;
    public String spell2;
    public String spell3;
    public ItemStack itemStack;

    public SoulScrollSpellSelectScreenHandler(int syncId, PlayerInventory playerInventory, SoulScrollDataPayload payload) {
        this(syncId, playerInventory, payload.itemStack());
        this.spell1 = payload.spell1();
        this.spell2 = payload.spell2();
        this.spell3 = payload.spell3();
        this.itemStack = payload.itemStack();
    }

    public SoulScrollSpellSelectScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack itemStack) {
        super(ScreenHandlerTypeInit.SOUL_SCROLL_SPELL_SELECT, syncId);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}