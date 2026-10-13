package smp_mod.smp_mod.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.SaveData.ServerData;

public class AllowTotems {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("allowtotems")
                .then(Commands.argument("bool", BoolArgumentType.bool())
                        .executes(context -> {

                            boolean bool = BoolArgumentType.getBool(context, "bool");

                            ServerLevel overworld = context.getSource().getLevel().getServer().overworld();

                            ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
                            if(!serverPlayer.level().getServer().getPlayerList().isOp(new NameAndId(serverPlayer.getGameProfile()))){
                                return 0;
                            }


                            ServerData serverData = overworld.getAttached(SMP_Mod.SERVER_DATA);
                            if(serverData == null){
                                serverData = new ServerData();
                                overworld.setAttached(SMP_Mod.SERVER_DATA,serverData);
                            }

                            overworld.setAttached(SMP_Mod.SERVER_DATA,new ServerData(
                                    serverData.playersToKill,
                                    serverData.playersAndDummys,
                                    serverData.carriedAndCarryers,
                                    serverData.blockCarriers,
                                    serverData.webHookURL,
                                    bool
                            ));

                            return 1;
                        }));
    }

}
