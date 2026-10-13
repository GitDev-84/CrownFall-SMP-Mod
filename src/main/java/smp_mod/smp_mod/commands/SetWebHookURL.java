package smp_mod.smp_mod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import org.jspecify.annotations.Nullable;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.SaveData.ServerData;

import java.util.logging.Level;

public class SetWebHookURL {

    public static LiteralArgumentBuilder<CommandSourceStack> register(){
        return Commands.literal("webhook")
                .then(Commands.literal("set")
                        .then(Commands.argument("URL", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String url = StringArgumentType.getString(context, "URL");

                                    MinecraftServer server = context.getSource().getServer();
                                    ServerLevel overworld = server.getLevel(ServerLevel.OVERWORLD);
                                    ServerPlayer serverPlayer = context.getSource().getPlayerOrException();

                                    ServerData serverData =  overworld.getAttached(SMP_Mod.SERVER_DATA);
                                    if(serverData == null){
                                        serverData = new ServerData();
                                    }


                                    if(!serverPlayer.level().getServer().getPlayerList().isOp(new NameAndId(serverPlayer.getGameProfile()))){
                                        return 0;
                                    }

                                    overworld.setAttached(SMP_Mod.SERVER_DATA,new ServerData(
                                            serverData.playersToKill,
                                            serverData.playersAndDummys,
                                            serverData.carriedAndCarryers,
                                            serverData.blockCarriers,
                                            url,
                                            serverData.allowTotems
                                    ));





                                    return 0;
                                })
                        )
                );
    }
}
