package smp_mod.smp_mod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import smp_mod.smp_mod.ModEntities;
import smp_mod.smp_mod.entity.DummyEntity;
import smp_mod.smp_mod.entity.GraveEntity;

public class SpawnDummyCommand {


    public static LiteralArgumentBuilder<CommandSourceStack> register(){
        return Commands.literal("spawnDummy")
                .executes(context -> {
                    CommandSourceStack source = context.getSource();

                    Player player = source.getPlayerOrException();

                    if(!player.level().getServer().getPlayerList().isOp(new NameAndId(player.getGameProfile()))){
                        return 0;
                    }

                    ServerLevel serverLevel = source.getLevel();
                    DummyEntity DUMMY = new DummyEntity(ModEntities.DUMMY, player.level());

                    DUMMY.syncWithPlayer(player);

                    serverLevel.addFreshEntity(DUMMY);


                    return 1;
                });
    }
}
