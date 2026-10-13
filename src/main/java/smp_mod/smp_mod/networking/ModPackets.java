package smp_mod.smp_mod.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class ModPackets {


    public static void registryClientbound(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry){



    }

    public static void registryServerbound(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry){



    }

    public static void registerPackets(){
        registryClientbound(PayloadTypeRegistry.clientboundPlay());
        registryServerbound(PayloadTypeRegistry.serverboundPlay());
    }


}
