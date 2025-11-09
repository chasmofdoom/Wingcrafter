package org.aussiebox.wingcrafter.item.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.component.SoulScrollSpells;
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.network.SoulScrollDataPayload;
import org.aussiebox.wingcrafter.screenhandler.SoulScrollSpellSelectScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SoulScrollItem extends Item implements ExtendedScreenHandlerFactory<SoulScrollDataPayload> {
    public SoulScrollItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        super.use(world, user, hand);
        user.openHandledScreen(this);
        return ActionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if (entity instanceof ServerPlayerEntity player) {
            if (stack.isOf(ModItems.SOUL_SCROLL)) {
                if (stack.get(ModDataComponentTypes.SOUL_SCROLL_OWNER) == null) {
                    stack.set(ModDataComponentTypes.SOUL_SCROLL_OWNER, player.getUuidAsString());
                }
                if (Objects.equals(stack.get(ModDataComponentTypes.SOUL_SCROLL_OWNER), player.getUuidAsString()) && !Objects.equals(stack.get(ModDataComponentTypes.SOUL_SCROLL_OWNER_NAME), player.getStringifiedName())) {
                    stack.set(ModDataComponentTypes.SOUL_SCROLL_OWNER_NAME, player.getStringifiedName());
                }
            }
        }
    }

    @Override
    public SoulScrollDataPayload getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        ItemStack itemStack = serverPlayerEntity.getInventory().getSelectedStack();
        if (itemStack.get(ModDataComponentTypes.SOUL_SCROLL_SPELLS) == null) {
            itemStack.set(ModDataComponentTypes.SOUL_SCROLL_SPELLS, new SoulScrollSpells("none", "none", "none"));
            serverPlayerEntity.getInventory().markDirty();
        }
        SoulScrollSpells spells = itemStack.get(ModDataComponentTypes.SOUL_SCROLL_SPELLS);

        return new SoulScrollDataPayload(itemStack, spells.spell1(), spells.spell2(), spells.spell3());
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SoulScrollSpellSelectScreenHandler(syncId, playerInventory, this.getDefaultStack());
    }
}
