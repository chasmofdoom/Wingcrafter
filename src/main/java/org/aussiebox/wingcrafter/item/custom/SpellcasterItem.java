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
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.network.SpellcasterDataPayload;
import org.aussiebox.wingcrafter.screenhandler.SpellcasterSpellSelectScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellcasterItem extends Item implements ExtendedScreenHandlerFactory<SpellcasterDataPayload> {
    public SpellcasterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        super.use(world, user, hand);
        ItemStack stack = user.getStackInHand(hand);

        if (stack.get(ModDataComponentTypes.SPELLCASTER_SPELLS) == null) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < stack.getOrDefault(ModDataComponentTypes.SPELLCASTER_SLOT_MAXIMUM, 3); i++) {
                list.add("none");
            }
            stack.set(ModDataComponentTypes.SPELLCASTER_SPELLS, list);
        }

        user.openHandledScreen(this);
        return ActionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);
        if (entity instanceof ServerPlayerEntity player) {
            if (stack.isOf(ModItems.SOUL_SCROLL)) {
                if (stack.get(ModDataComponentTypes.SPELLCASTER_OWNER) == null) {
                    stack.set(ModDataComponentTypes.SPELLCASTER_OWNER, player.getUuidAsString());
                }
                if (Objects.equals(stack.get(ModDataComponentTypes.SPELLCASTER_OWNER), player.getUuidAsString()) && !Objects.equals(stack.get(ModDataComponentTypes.SPELLCASTER_OWNER_NAME), player.getStringifiedName())) {
                    stack.set(ModDataComponentTypes.SPELLCASTER_OWNER_NAME, player.getStringifiedName());
                }
            }
        }
    }

    @Override
    public SpellcasterDataPayload getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        ItemStack itemStack = serverPlayerEntity.getInventory().getSelectedStack();
        if (itemStack.get(ModDataComponentTypes.SPELLCASTER_SPELLS) == null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) list.add("none");
            itemStack.set(ModDataComponentTypes.SPELLCASTER_SLOT_MAXIMUM, 3);
            itemStack.set(ModDataComponentTypes.SPELLCASTER_SPELLS, list);
            serverPlayerEntity.getInventory().markDirty();
        }
        List<String> spells = itemStack.get(ModDataComponentTypes.SPELLCASTER_SPELLS);

        return new SpellcasterDataPayload(itemStack, spells);
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SpellcasterSpellSelectScreenHandler(syncId, playerInventory, this.getDefaultStack());
    }
}
