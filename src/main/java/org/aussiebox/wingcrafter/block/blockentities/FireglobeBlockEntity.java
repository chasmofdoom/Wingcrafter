package org.aussiebox.wingcrafter.block.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.aussiebox.wingcrafter.block.ModBlockEntities;

public class FireglobeBlockEntity extends BlockEntity {
    private String front = "white";
    private String left = "white";
    private String back = "white";
    private String right = "white";

    public FireglobeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FIREGLOBE_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeData(WriteView writeView) {
        writeView.putString("front", front);
        writeView.putString("left", left);
        writeView.putString("back", back);
        writeView.putString("right", right);
        super.writeData(writeView);
    }

    @Override
    protected void readData(ReadView readView) {
        super.readData(readView);
        front = readView.getString("front", "Data Read Failed.");
        left = readView.getString("left", "Data Read Failed.");
        back = readView.getString("back", "Data Read Failed.");
        right = readView.getString("right", "Data Read Failed.");
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
