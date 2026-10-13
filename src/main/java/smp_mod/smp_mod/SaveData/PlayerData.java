package smp_mod.smp_mod.SaveData;

import com.jcraft.jorbis.Block;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.sync.SyncHeartsPayload;

public class PlayerData {

    public int inFight = 0;
    public int hearts = 3;
    public int enderPearlsToUse = 32;
    public String gotHitByUUID;

    public final void tick(ServerPlayer player) {
        if (this.inFight > 0) {
            this.inFight--;
            ServerPlayNetworking.send(player, new SyncHeartsPayload(this));
        } else if (this.inFight == 0) {
            player.setAttached(SMP_Mod.PLAYER_DATA, new PlayerData(
                    this.hearts,
                    this.inFight,
                    32,
                    this.gotHitByUUID
            ));
        }
    }

    public PlayerData(int hearts, int inFight, int enderPearlsToUse , String gotHitByUUID) {
        this.hearts = hearts;
        this.inFight = inFight;
        this.enderPearlsToUse = enderPearlsToUse;
        this.gotHitByUUID = gotHitByUUID;
    }

    public PlayerData() {
        this.hearts = 3;
        this.inFight = 0;
        this.enderPearlsToUse = 32;
        this.gotHitByUUID = "";
    }

    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("hearts").forGetter(d -> d.hearts),
                    Codec.INT.fieldOf("inFight").forGetter(d -> d.inFight),
                    Codec.INT.fieldOf("enderPearlsToUse").forGetter(d -> d.enderPearlsToUse),
                    Codec.STRING.fieldOf("gotHitBy").forGetter(d->d.gotHitByUUID)
            ).apply(instance, PlayerData::new)
    );

    public static final CustomPacketPayload.Type<SyncHeartsPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("smp_mod", "sync_hearts"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncHeartsPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.fromCodec(PlayerData.CODEC).cast(), SyncHeartsPayload::attachment,
                    SyncHeartsPayload::new
            );
}

//UUID.fromString(graveUUID); uuid.toString()  level.getEntity(uuid); grave.getUUID()
