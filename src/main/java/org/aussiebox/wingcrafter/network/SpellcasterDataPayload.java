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

public record SpellcasterDataPayload(ItemStack itemStack, List<String> spellList) implements CustomPayload {
    public static final Identifier PAYLOAD_ID = Identifier.of(Wingcrafter.MOD_ID, "spellcaster_data");
    public static final Id<SpellcasterDataPayload> ID = new Id<>(PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, SpellcasterDataPayload> PACKET_CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, SpellcasterDataPayload::itemStack, PacketCodecs.codec(Codec.list(Codec.STRING)), SpellcasterDataPayload::spellList, SpellcasterDataPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}