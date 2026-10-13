package smp_mod.smp_mod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.SaveData.PlayerData;
import smp_mod.smp_mod.entity.GraveEntity;

import java.util.UUID;

public class CheckCommand {
    public CheckCommand(){}





    public static LiteralArgumentBuilder<CommandSourceStack> getCommand(){
        return Commands.literal("check")
                .then(Commands.literal("hearts")
                        .executes(context -> {

                            CommandSourceStack source = context.getSource();

                            ServerPlayer serverPlayer = source.getPlayer();

                            PlayerData playerData = serverPlayer.getAttached(SMP_Mod.PLAYER_DATA);
                            if (playerData == null) {
                                playerData = new PlayerData();
                                serverPlayer.setAttached(SMP_Mod.PLAYER_DATA, playerData);
                            }


                            serverPlayer.sendSystemMessage(Component.literal("Du hast noch " + playerData.hearts + " Herzen"));

                            return 1;
                        })
                )
                .then(Commands.literal("timeInCombat")
                        .executes(context -> {

                            CommandSourceStack source = context.getSource();

                            ServerPlayer serverPlayer = source.getPlayer();

                            PlayerData playerData = serverPlayer.getAttached(SMP_Mod.PLAYER_DATA);

                            if (playerData == null) {
                                playerData = new PlayerData();
                                serverPlayer.setAttached(SMP_Mod.PLAYER_DATA, playerData);
                            }

                            final PlayerData playerDataFinal = playerData;

                            source.sendSuccess(() -> Component.literal("Du bist noch " + playerDataFinal.inFight + " Sekunden im Kampf."), false);

                            return 1;
                        })
                );
    }

}
