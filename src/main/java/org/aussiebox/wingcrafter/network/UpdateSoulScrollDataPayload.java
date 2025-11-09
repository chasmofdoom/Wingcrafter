package org.aussiebox.wingcrafter.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public record UpdateSoulScrollDataPayload(ItemStack itemStack, String spell1, String spell2, String spell3) implements CustomPayload {
    public static final Identifier UPDATE_SOUL_SCROLL_DATA_ID = Identifier.of(Wingcrafter.MOD_ID, "update_soul_scroll_data");
    public static final Id<UpdateSoulScrollDataPayload> ID = new Id<>(UPDATE_SOUL_SCROLL_DATA_ID);
    public static final PacketCodec<RegistryByteBuf, UpdateSoulScrollDataPayload> PACKET_CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, UpdateSoulScrollDataPayload::itemStack, PacketCodecs.STRING, UpdateSoulScrollDataPayload::spell1, PacketCodecs.STRING, UpdateSoulScrollDataPayload::spell2, PacketCodecs.STRING, UpdateSoulScrollDataPayload::spell3, UpdateSoulScrollDataPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}