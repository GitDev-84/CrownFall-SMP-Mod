package smp_mod.smp_mod.sync;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import smp_mod.smp_mod.SaveData.PlayerData;

public record SyncHeartsPayload(PlayerData attachment) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncHeartsPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("smp_mod", "sync_hearts"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncHeartsPayload> CODEC = StreamCodec.of(
            (buf, payload) -> {
                PlayerData data = (payload.attachment() != null) ? payload.attachment() : new PlayerData();
                buf.writeInt(data.hearts);
                buf.writeInt(data.inFight);
            },
            buf -> {
                int hearts = buf.readInt();
                int inFight = buf.readInt();

                PlayerData data = new PlayerData();
                data.hearts = hearts;
                data.inFight = inFight;
                return new SyncHeartsPayload(data);
            }
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}