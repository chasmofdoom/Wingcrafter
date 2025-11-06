package org.aussiebox.wingcrafter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.attach.ModAttachmentTypes;
import org.aussiebox.wingcrafter.attach.ModCustomAttachedData;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.aussiebox.wingcrafter.item.ModItems;

public class HudRenderingEntrypoint implements ClientModInitializer {
    static Text scrollTitle = Text.of("Untitled Scroll");
    static Text scrollLineCountText = Text.of("Line Count: N/A");
    static Text changeText = Text.of("idk bro 🥀");
    static int scrollLineCount = 0;
    static int change = 0;
    static int changeTime = 0;
    static int changeFadeIn;
    static int changeFadeOut;
    static Text warningText = Text.of("Warning! This is a warning!");
    static int warningTime = 0;
    static int warningFadeIn;
    static int warningFadeOut;
    static int lastSoul;
    static boolean lastVisibility = false;

    public static final Identifier BACKGROUND = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/background.png");
    public static final Identifier PROGRESS = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/progress.png");

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.FOOD_BAR, Identifier.of(Wingcrafter.MOD_ID, "soul_bar"), HudRenderingEntrypoint::renderSoul);
        HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Identifier.of(Wingcrafter.MOD_ID, "scroll_tooltip"), HudRenderingEntrypoint::renderScroll);
    }

    private static void renderSoul(DrawContext context, RenderTickCounter tickCounter) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        ModCustomAttachedData data = player.getAttachedOrSet(ModAttachmentTypes.SOUL_ATTACH, ModCustomAttachedData.DEFAULT);
        int soul = data.soul();
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        if (player.getInventory().contains(new ItemStack(ModItems.SOUL_SCROLL))) {
            int progressEquation = 71-(soul*71/1000);

            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    BACKGROUND,
                    (int) ((double) width/2+12.5),
                    (int) ((double) height-55),
                    (float) 0,
                    (float) 0,
                    75,
                    12,
                    75,
                    12
            );
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    PROGRESS,
                    (int) ((double) width/2+14.5),
                    (int) ((double) height-53),
                    (float) 71,
                    (float) 0,
                    progressEquation,
                    8,
                    progressEquation,
                    8,
                    71,
                    8
            );
            if (soul > lastSoul) {
                if (changeFadeIn == 0 && changeTime == 0 && changeFadeOut == 0) {
                    change = soul - lastSoul;
                    changeFadeIn = 20;
                    changeTime = 40;
                    changeFadeOut = 20;
                } else {
                    change -= lastSoul - soul;
                    changeTime = 40;
                    changeFadeOut = 20;
                }
            } else if (soul < lastSoul) {
                if (changeFadeIn == 0 && changeTime == 0 && changeFadeOut == 0) {
                    change = soul - lastSoul;
                    changeFadeIn = 20;
                    changeTime = 40;
                    changeFadeOut = 20;
                } else {
                    change += soul - lastSoul;
                    changeTime = 40;
                    changeFadeOut = 20;
                }
            }

            if (soul <= 500 && lastSoul > 500) {
                if (warningFadeIn == 0 && warningTime == 0 && warningFadeOut == 0) {
                    warningText = Text.literal("I feel cold.");
                    warningFadeIn = 20;
                    warningTime = 80;
                    warningFadeOut = 20;
                }
            }

            int changeX = (int) ((int) ((double) width/2+12.5) - ((double) textRenderer.getWidth(changeText) / 2) + 37.5);
            int changeY = (int) ((double) height-55) - 10;

            int warningX = width/2 - (textRenderer.getWidth(warningText) / 2);
            int warningY = height/10*7 - (textRenderer.fontHeight / 2);

            if (change >= 0) {
                changeText = Text.of("+" + change);
            } else {
                changeText = Text.of(String.valueOf(change));
            }

            if (change == 0) {
                changeText = Text.of("=0");
            }

            if (changeFadeIn > 0) {
                context.drawText(textRenderer, changeText, changeX - changeFadeIn, changeY, ColorHelper.lerp((float) (1.0-((double) changeFadeIn / 20)), 0x00FFFFFF, 0xFFFFFFFF), true);
                changeFadeIn--;
            } else if (changeTime > 0) {
                context.drawText(textRenderer, changeText, changeX, changeY, 0xFFFFFFFF, true);
                changeTime--;
            } else if (changeFadeOut > 0) {
                context.drawText(textRenderer, changeText, changeX + (-1*changeFadeOut) + 20, changeY, ColorHelper.lerp((float) (1.0-((double) changeFadeOut / 20)), 0xFFFFFFFF, 0x00FFFFFF), true);
                changeFadeOut--;
            }

            if (warningFadeIn > 0) {
                context.drawText(textRenderer, warningText, warningX, warningY + (warningFadeIn/2), ColorHelper.lerp((float) (1.0-((double) warningFadeIn / 20)), 0x00FFFFFF, 0xFFFFFFFF), true);
                warningFadeIn--;
            } else if (warningTime > 0) {
                context.drawText(textRenderer, warningText, warningX, warningY, 0xFFFFFFFF, true);
                warningTime--;
            } else if (warningFadeOut > 0) {
                context.drawText(textRenderer, warningText, warningX, warningY + (warningFadeOut/2) - 10, ColorHelper.lerp((float) (1.0-((double) warningFadeOut / 20)), 0xFFFFFFFF, 0x00FFFFFF), true);
                warningFadeOut--;
            }

            // Somehow, adding the "change" rendering system took me longer than the soul bar itself.
            // My life is full of pain, suffering, and rainbows.

            lastVisibility = true;
            lastSoul = data.soul();
        } else {
            lastVisibility = false;
            lastSoul = data.soul();
        }
    }

    private static void renderScroll(DrawContext context, RenderTickCounter tickCounter) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        int scrollTitleX = width/2 - (textRenderer.getWidth(scrollTitle)/2);
        int scrollTitleY = height/2 - (textRenderer.fontHeight/2) + 15;

        int scrollLineCountX = width/2 - (textRenderer.getWidth(scrollLineCountText)/2);

        HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;

        if (hitResult != null) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                World world = player.getEntityWorld();
                if (world != null) {
                    BlockEntity blockEntity = world.getBlockEntity(BlockPos.ofFloored(hitResult.getPos()));
                    if (blockEntity instanceof ScrollBlockEntity scrollBlockEntity) {
                        scrollTitle = Text.of("\"" + scrollBlockEntity.getTitle() + "\"");
                        scrollLineCount = (int) scrollBlockEntity.getText().lines().count();
                        scrollLineCountText = Text.of("Line Count: " + scrollLineCount);
                        context.drawText(textRenderer, scrollTitle, scrollTitleX, scrollTitleY, 0xFFFFFFFF, true);
                        context.drawText(textRenderer, scrollLineCountText, scrollLineCountX, scrollTitleY + 11, 0xFFAAAAAA, true);
                    }
                }
            }
        }
    }
}
