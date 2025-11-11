package org.aussiebox.wingcrafter.block.blockentities.renderer;

import net.minecraft.client.model.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.blockentities.FireglobeBlockEntity;
import org.aussiebox.wingcrafter.client.WingcrafterClient;

import java.util.EnumSet;

public class FireglobeBlockEntityRenderer implements BlockEntityRenderer<FireglobeBlockEntity, FireglobeBlockEntityRenderState> {
    public static final EntityModelLayer FIREGLOBE_SIDES = new EntityModelLayer(Identifier.of(Wingcrafter.MOD_ID, "globe"), "main");
    public static final SpriteIdentifier BLUE = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "blue"));
    private final ModelPart front;
    private final ModelPart back;
    private final ModelPart left;
    private final ModelPart right;
    private final SpriteHolder materials;

    public FireglobeBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        ModelPart modelPart = context.getLayerModelPart(FIREGLOBE_SIDES);
        this.front = modelPart.getChild("front");
        this.back = modelPart.getChild("back");
        this.left = modelPart.getChild("left");
        this.right = modelPart.getChild("right");
        this.materials = context.spriteHolder();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(4.0F, 2.0F, 4.0F, 8.0F, 8.0F, 8.0F, EnumSet.of(Direction.NORTH));
        modelPartData.addChild("back", modelPartBuilder, ModelTransform.of(8.0F, 12.0F, 1.0F, 0.0F, 0.0F, (float) Math.PI));
        modelPartData.addChild("left", modelPartBuilder, ModelTransform.of(0.0F, 12.0F, 1.0F, 0.0F, (float) (-Math.PI / 2), (float) Math.PI));
        modelPartData.addChild("right", modelPartBuilder, ModelTransform.of(8.0F, 12.0F, 15.0F, 0.0F, (float) (Math.PI / 2), (float) Math.PI));
        modelPartData.addChild("front", modelPartBuilder, ModelTransform.of(0.0F, 12.0F, 16.0F, (float) Math.PI, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public FireglobeBlockEntityRenderState createRenderState() {
        return new FireglobeBlockEntityRenderState();
    }

    @Override
    public void render(FireglobeBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        queue.submitModelPart(
                this.front,
                matrices,
                RenderLayer.getEntityCutout(WingcrafterClient.FIREGLOBE_GLASS_ATLAS_PATH),
                state.lightmapCoordinates,
                OverlayTexture.DEFAULT_UV,
                this.materials.getSprite(BLUE)
        );
        matrices.pop();
    }
}