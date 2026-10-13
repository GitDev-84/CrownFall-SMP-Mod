package smp_mod.smp_mod.mixin;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smp_mod.smp_mod.MixinHelperMethods.CmdList;
import smp_mod.smp_mod.MixinHelperMethods.ContainsString;
import smp_mod.smp_mod.MixinHelperMethods.DiscordAdminCommandSender;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.SaveData.ServerData;

import java.util.List;

@Mixin(Commands.class)
public class TrackAdminCommandsMixin {




    @Inject(
            method = "performCommand",
            at = @At("HEAD")
    )
    private void commandGetsExecuted(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfo ci) {
        System.out.println("signed command got Executed");
        ServerPlayer player = parseResults.getContext().getSource().getPlayer();
        if (player == null) {
            return;
        }

        MinecraftServer server = player.level().getServer();
        ServerLevel overworld = server.getLevel(ServerLevel.OVERWORLD);

        ServerData serverData = overworld.getAttached(SMP_Mod.SERVER_DATA);

        if(serverData == null){
            serverData = new ServerData();
            overworld.setAttached(SMP_Mod.SERVER_DATA,serverData);
        }


        if (ContainsString.ListContainsSubString(command, CmdList.opCmds)) {
            if (serverData.webHookURL != null && !serverData.webHookURL.isBlank()) {
                System.out.println("Should get sent");
                DiscordAdminCommandSender.send(serverData.webHookURL, player.getName().getString() + " hat " + command + " ausgeführt");
            } else {
                System.out.println("No Webhook URL");
            }
        }
    }





}