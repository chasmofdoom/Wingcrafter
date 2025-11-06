package org.aussiebox.wingcrafter.attach;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ModCustomAttachedData(int soul) {
    public static Codec<ModCustomAttachedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("soul").forGetter(ModCustomAttachedData::soul)
    ).apply(instance, ModCustomAttachedData::new));
    public static PacketCodec<ByteBuf, ModCustomAttachedData> PACKET_CODEC = PacketCodecs.codec(CODEC);

    public static ModCustomAttachedData DEFAULT = new ModCustomAttachedData(1000);

    public ModCustomAttachedData setSoul(int set) {
            return new ModCustomAttachedData(Math.clamp(set, 0, 1000));
    }

    public ModCustomAttachedData addSoul(ModCustomAttachedData data, int add) {
        return new ModCustomAttachedData(Math.clamp(data.soul() + add, 0, 1000));
    }

    public ModCustomAttachedData removeSoul(ModCustomAttachedData data, int remove) {
        return new ModCustomAttachedData(Math.clamp(data.soul() - remove, 0, 1000));
    }
}
