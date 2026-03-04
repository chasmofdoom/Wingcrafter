package org.aussiebox.wingcrafter.network;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

import java.util.List;

public record UpdateSpellcasterDataPayload(ItemStack itemStack, List<String> spellList) implements CustomPayload {
    public static final Identifier UPDATE_SOUL_SCROLL_DATA_ID = Identifier.of(Wingcrafter.MOD_ID, "update_soul_scroll_data");
    public static final Id<UpdateSpellcasterDataPayload> ID = new Id<>(UPDATE_SOUL_SCROLL_DATA_ID);
    public static final PacketCodec<RegistryByteBuf, UpdateSpellcasterDataPayload> PACKET_CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, UpdateSpellcasterDataPayload::itemStack, PacketCodecs.codec(Codec.list(Codec.STRING)), UpdateSpellcasterDataPayload::spellList, UpdateSpellcasterDataPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}