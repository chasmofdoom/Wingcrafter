package org.aussiebox.wingcrafter.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public record CastSpellPayload(String spellID) implements CustomPayload {
    public static final Identifier CAST_SPELL_ID = Identifier.of(Wingcrafter.MOD_ID, "cast_spell");
    public static final Id<CastSpellPayload> ID = new Id<>(CAST_SPELL_ID);
    public static final PacketCodec<RegistryByteBuf, CastSpellPayload> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.STRING, CastSpellPayload::spellID, CastSpellPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}