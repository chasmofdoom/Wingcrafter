package org.aussiebox.wingcrafter.client.screen;

import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.network.UpdateSpellcasterDataPayload;
import org.aussiebox.wingcrafter.screenhandler.SpellcasterSpellSelectScreenHandler;
import org.aussiebox.wingcrafter.spells.util.Spell;
import org.aussiebox.wingcrafter.spells.util.SpellRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class SpellcasterSpellSelectScreen extends BaseOwoHandledScreen<FlowLayout, SpellcasterSpellSelectScreenHandler> {
    public int selectedSlot = 0;
    Object2ObjectMap<Integer, ButtonComponent> spellButtons = new Object2ObjectLinkedOpenHashMap<>();

    public static final int buttonSize = 24;
    public static final Identifier SELECTED = Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/gamemode_switcher/selection.png");
    public static final Identifier UNSELECTED = Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_select/slot.png");

    Text titleText = Text.of("Spell Selection");

    public SpellcasterSpellSelectScreen(SpellcasterSpellSelectScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleX = -10000;
    }

    private final GridLayout buttonGrid = (GridLayout) Containers.grid(Sizing.content(), Sizing.content(), 1000000, 9)
            .padding(Insets.of(10))
            .surface(Surface.DARK_PANEL)
            .verticalAlignment(VerticalAlignment.TOP)
            .horizontalAlignment(HorizontalAlignment.CENTER);
    private GridLayout currentButtonGrid;

    private final FlowLayout selectionFlow = (FlowLayout) Containers.horizontalFlow(Sizing.content(), Sizing.content())
            .padding(Insets.of(10))
            .surface(Surface.BLANK)
            .verticalAlignment(VerticalAlignment.TOP)
            .horizontalAlignment(HorizontalAlignment.CENTER);
    private FlowLayout currentSelectionFlow;

    Map<String, Spell> spellMap = new LinkedHashMap<>();
    List<String> selectedSpells = new ArrayList<>(this.handler.spells);

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        try {
            spellMap = SpellRegistry.getSpellMap();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setSelectionFlow(selectionFlow);
        setButtonGrid(buttonGrid);
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);
        rootComponent.child(Components.label(titleText))
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.TOP)
                .padding(Insets.of(15, 10, 10, 10));
        rootComponent.child(getSelectionFlow())
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.TOP);
        rootComponent.child(getButtonGrid())
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.TOP);
    }

    @Override
    public void close() {
        UpdateSpellcasterDataPayload payload = new UpdateSpellcasterDataPayload(this.handler.itemStack, selectedSpells);
        ClientPlayNetworking.send(payload);
        super.close();
    }

    public GridLayout getButtonGrid() {
        return this.currentButtonGrid;
    }

    public void setButtonGrid(GridLayout grid) {
        this.currentButtonGrid = grid;
        int row = 1;
        int column = 0;
        for (String id : spellMap.keySet()) {
            column++;
            if (column > 8) {
                row++;
                column = 1;
            }
            this.currentButtonGrid.child(Components.button(
                                    Text.empty(),
                                    button -> {
                                        if (selectedSpells.stream().noneMatch(Predicate.isEqual(id))) {
                                            ButtonComponent oldSpell = this.currentButtonGrid.childById(ButtonComponent.class, "spell_" + selectedSpells.get(selectedSlot));
                                            ButtonComponent newSpell = this.currentButtonGrid.childById(ButtonComponent.class, "spell_" + id);

                                            if (oldSpell != null) {
                                                oldSpell.renderer(ButtonComponent.Renderer.texture(
                                                        spellMap.get(selectedSpells.get(selectedSlot)).getButtonTexture(),
                                                        0,
                                                        0,
                                                        buttonSize,
                                                        buttonSize
                                                ));
                                            }
                                            selectedSpells.set(selectedSlot, id);
                                            newSpell.renderer(ButtonComponent.Renderer.texture(
                                                    selectedSpells.stream().anyMatch(Predicate.isEqual(id)) ? Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_selected.png") : spellMap.get(id).getButtonTexture(),
                                                    0,
                                                    0,
                                                    buttonSize,
                                                    buttonSize
                                            ));

                                            setSelectionFlow(selectionFlow);
                                        }
                                    }
                            )
                            .renderer(ButtonComponent.Renderer.texture(
                                    selectedSpells.stream().anyMatch(Predicate.isEqual(id)) ? Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_selected.png") : spellMap.get(id).getButtonTexture(),
                                    0,
                                    0,
                                    buttonSize,
                                    buttonSize
                            ))
                            .sizing(Sizing.fixed(buttonSize))
                            .margins(Insets.of(5))
                            .id("spell_" + id),
                    row, column);
        }
    }

    public FlowLayout getSelectionFlow() {
        return this.currentSelectionFlow;
    }

    public void setSelectionFlow(FlowLayout flow) {
        this.currentSelectionFlow = flow;
        this.currentSelectionFlow.clearChildren();
        for (int i = 0; i < selectedSpells.size(); i++) {
            int index = i;
            this.currentSelectionFlow.child(Components.button(
                                    Text.empty(),
                                    button -> {
                                        if (selectedSlot == index) {
                                            ButtonComponent oldSpell = this.currentButtonGrid.childById(ButtonComponent.class, "spell_" + selectedSpells.get(selectedSlot));
                                            if (oldSpell != null) {
                                                oldSpell.renderer(ButtonComponent.Renderer.texture(
                                                        spellMap.get(selectedSpells.get(selectedSlot)).getButtonTexture(),
                                                        0,
                                                        0,
                                                        buttonSize,
                                                        buttonSize
                                                ));
                                            }

                                            selectedSpells.set(selectedSlot, "none");
                                        }
                                        selectedSlot = index;
                                        setSelectionFlow(selectionFlow);
                                    }
                            )
                            .renderer(ButtonComponent.Renderer.texture(
                                    selectedSlot == index ? SELECTED : UNSELECTED,
                                    0,
                                    0,
                                    44,
                                    44
                            ))
                            .sizing(Sizing.fixed(44))
                            .margins(Insets.of(4))
                            .id("spell_slot_" + index)
            );
            spellButtons.put(index, this.currentSelectionFlow.childById(ButtonComponent.class, "spell_slot_" + index));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (String spellID : selectedSpells) {
            int slot = selectedSpells.indexOf(spellID);
            ButtonComponent button = spellButtons.get(slot);

            if (!spellMap.containsKey(spellID)) continue;

            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    spellMap.get(spellID).getButtonTexture(),
                    button.getX() + 5,
                    button.getY() + 5,
                    0,
                    0,
                    button.getWidth() - 10,
                    button.getHeight() - 10,
                    button.getWidth() - 10,
                    button.getHeight() - 10
            );
        }

        for (Component component : this.currentButtonGrid.children()) {
            if (!(component instanceof ButtonComponent button)) continue;
            if (button.id() == null) continue;
            if (!Objects.requireNonNull(button.id()).startsWith("spell_")) continue;
            String spellID = Objects.requireNonNull(button.id()).replace("spell_", "");

            if (selectedSpells.stream().anyMatch(Predicate.isEqual(spellID))) continue;
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    UNSELECTED,
                    button.getX() - 3,
                    button.getY() - 3,
                    0,
                    0,
                    button.getWidth() + 6,
                    button.getHeight() + 6,
                    button.getWidth() + 6,
                    button.getHeight() + 6
            );
        }
    }
}
