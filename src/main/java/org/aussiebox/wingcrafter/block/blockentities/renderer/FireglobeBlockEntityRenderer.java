package org.aussiebox.wingcrafter.block.blockentities.renderer;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.block.blockentities.FireglobeBlockEntity;

import java.util.EnumSet;
import java.util.Map;

public class FireglobeBlockEntityRenderer implements BlockEntityRenderer<FireglobeBlockEntity, FireglobeBlockEntityRenderState> {
    public static final EntityModelLayer FIREGLOBE_SIDES = new EntityModelLayer(Identifier.of("globe"), "main")
    private final SpriteHolder materials;
    private final ModelPart front;
    private final ModelPart back;
    private final ModelPart left;
    private final ModelPart right;

    public FireglobeBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        LoadedEntityModels loadedEntityModels = context.loadedEntityModels();
        ModelPart modelPart = loadedEntityModels.getModelPart(FIREGLOBE_SIDES);
        this.materials = context.spriteHolder();
        this.front = modelPart.getChild("front");
        this.back = modelPart.getChild("back");
        this.left = modelPart.getChild("left");
        this.right = modelPart.getChild("right");
    }

    public static Map<EntityModelLayer, TexturedModelData> getModels() {
        ImmutableMap.Builder<EntityModelLayer, TexturedModelData> builder = ImmutableMap.builder();
        builder.put(FIREGLOBE_SIDES, getSidesTexturedModelData());
        return builder.build();
    }

    public static TexturedModelData getSidesTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(1, 0).cuboid(4.0F, 2.0F, 4.0F, 8.0F, 8.0F, 0.0F, EnumSet.of(Direction.NORTH));
        modelPartData.addChild("back", modelPartBuilder, ModelTransform.of(7.0F, 8.0F, 1.0F, 0.0F, 0.0F, (float) Math.PI));
        modelPartData.addChild("left", modelPartBuilder, ModelTransform.of(1.0F, 8.0F, 1.0F, 0.0F, (float) (-Math.PI / 2), (float) Math.PI));
        modelPartData.addChild("right", modelPartBuilder, ModelTransform.of(7.0F, 8.0F, 7.0F, 0.0F, (float) (Math.PI / 2), (float) Math.PI));
        modelPartData.addChild("front", modelPartBuilder, ModelTransform.of(1.0F, 8.0F, 7.0F, (float) Math.PI, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 16, 16);
    }


    @Override
    public FireglobeBlockEntityRenderState createRenderState() {
        return null;
    }

    @Override
    public void render(FireglobeBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {

    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
