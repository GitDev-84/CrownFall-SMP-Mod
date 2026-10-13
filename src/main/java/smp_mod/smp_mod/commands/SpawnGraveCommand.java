package smp_mod.smp_mod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import smp_mod.smp_mod.ModEntities;
import smp_mod.smp_mod.entity.GraveEntity;

import java.util.logging.Level;

public class SpawnGraveCommand {

    public SpawnGraveCommand(){}

    public static LiteralArgumentBuilder<CommandSourceStack>  SpawnGrave(){
        return Commands.literal("spawnGrave")
                .executes(context -> {
                    CommandSourceStack source = context.getSource();

                    Player serverPlayer = source.getPlayerOrException();
                    ServerLevel serverLevel = source.getLevel();

                    if(!serverPlayer.level().getServer().getPlayerList().isOp(new NameAndId(serverPlayer.getGameProfile()))){
                        return 0;
                    }

                    Vec3 playerPos = serverPlayer.getPosition(1.0F);

                    GraveEntity GRAVE = new GraveEntity(ModEntities.GRAVE, serverPlayer.level());
                    GRAVE.setCustomName(null);
                    serverLevel.addFreshEntity(GRAVE);
                    GRAVE.setPos(playerPos);

                    return 1;
                });
    }
}
