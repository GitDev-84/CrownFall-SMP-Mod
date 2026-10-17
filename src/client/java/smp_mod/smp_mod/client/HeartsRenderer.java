package smp_mod.smp_mod.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.SaveData.PlayerData;
import smp_mod.smp_mod.sync.SyncHeartsPayload;


public class HeartsRenderer {


    public static void register(GuiGraphicsExtractor guiGraphicsExtractor, DeltaTracker deltaTracker){

        String pathCyanHeart = "textures/hearts/cyan_heart.png";
        String pathLostHeart = "textures/hearts/lost_heart.png";


        Player player = Minecraft.getInstance().player;
        if(player == null){
            return;
        }
        PlayerData playerData = player.getAttached(SMP_Mod.PLAYER_DATA);

        if(playerData == null){
            playerData = new PlayerData();
            player.setAttached(SMP_Mod.PLAYER_DATA,playerData);
        }

        if(playerData.hearts == 5){
            drawLowerLeftHeart(guiGraphicsExtractor,pathCyanHeart);
            drawLowerRightHeart(guiGraphicsExtractor,pathCyanHeart);
            drawUpperHeart(guiGraphicsExtractor,pathCyanHeart);
            drawUpperLeftHeart(guiGraphicsExtractor,pathCyanHeart);
            drawUpperRightHeart(guiGraphicsExtractor,pathCyanHeart);

        }else if(playerData.hearts == 4){
            drawLowerLeftHeart(guiGraphicsExtractor,pathCyanHeart);
            drawLowerRightHeart(guiGraphicsExtractor,pathCyanHeart);
            drawUpperHeart(guiGraphicsExtractor,pathCyanHeart);
            drawUpperLeftHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperRightHeart(guiGraphicsExtractor,pathCyanHeart);
        }else if(playerData.hearts == 3){
            drawLowerLeftHeart(guiGraphicsExtractor,pathCyanHeart);
            drawLowerRightHeart(guiGraphicsExtractor,pathCyanHeart);
            drawUpperHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperLeftHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperRightHeart(guiGraphicsExtractor,pathCyanHeart);
        }else if(playerData.hearts == 2){
            drawLowerLeftHeart(guiGraphicsExtractor,pathCyanHeart);
            drawLowerRightHeart(guiGraphicsExtractor,pathCyanHeart);
            drawUpperHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperLeftHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperRightHeart(guiGraphicsExtractor,pathLostHeart);
        }else if(playerData.hearts == 1){
            drawLowerLeftHeart(guiGraphicsExtractor,pathLostHeart);
            drawLowerRightHeart(guiGraphicsExtractor,pathCyanHeart);
            drawUpperHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperLeftHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperRightHeart(guiGraphicsExtractor,pathLostHeart);
        }else if(playerData.hearts == 0){
            drawLowerLeftHeart(guiGraphicsExtractor,pathLostHeart);
            drawLowerRightHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperLeftHeart(guiGraphicsExtractor,pathLostHeart);
            drawUpperRightHeart(guiGraphicsExtractor,pathLostHeart);
        }

        if(playerData.inFight>0){
            drawTimeInCombat(guiGraphicsExtractor,playerData.inFight);

        }



    }

    public static void drawTimeInCombat(GuiGraphicsExtractor guiGraphicsExtractor, int inFight){

        Font font = Minecraft.getInstance().font;

        Window window = Minecraft.getInstance().getWindow();

        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();

        int x = screenWidth/2;
        int y = screenHeight-80;

        guiGraphicsExtractor.centeredText(
                font,
                Component.literal("Du bist noch " + inFight + " Sekunden im Kampf!"),
                x,
                y,
                0xFF8B0000


        );


    }

    public static void drawUpperHeart(GuiGraphicsExtractor guiGraphicsExtractor, String path){

        Identifier imgLocation = Identifier.fromNamespaceAndPath(SMP_Mod.MOD_ID,path);

        Window window = Minecraft.getInstance().getWindow();

        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();
        int heartSize = 12;
        int x = screenWidth/2-heartSize/2;
        int y = screenHeight-50-heartSize;

        guiGraphicsExtractor.blit(
                RenderPipelines.GUI_TEXTURED,
                imgLocation,
                x,
                y,
                0,
                0,
                heartSize,
                heartSize,
                12,
                12



        );

    }

    public static void drawUpperLeftHeart(GuiGraphicsExtractor guiGraphicsExtractor, String path){

        Identifier imgLocation = Identifier.fromNamespaceAndPath(SMP_Mod.MOD_ID,path);

        Window window = Minecraft.getInstance().getWindow();

        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();
        int heartSize = 12;
        int x = (screenWidth/2-heartSize/2)-12;
        int y = screenHeight-50-heartSize;

        guiGraphicsExtractor.blit(
                RenderPipelines.GUI_TEXTURED,
                imgLocation,
                x,
                y,
                0,
                0,
                heartSize,
                heartSize,
                12,
                12



        );

    }

    public static void drawUpperRightHeart(GuiGraphicsExtractor guiGraphicsExtractor, String path){

        Identifier imgLocation = Identifier.fromNamespaceAndPath(SMP_Mod.MOD_ID,path);

        Window window = Minecraft.getInstance().getWindow();

        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();
        int heartSize = 12;
        int x = (screenWidth/2-heartSize/2)+12;
        int y = screenHeight-50-heartSize;

        guiGraphicsExtractor.blit(
                RenderPipelines.GUI_TEXTURED,
                imgLocation,
                x,
                y,
                0,
                0,
                heartSize,
                heartSize,
                12,
                12



        );

    }

    public static void drawLowerLeftHeart(GuiGraphicsExtractor guiGraphicsExtractor, String path){

        Identifier imgLocation = Identifier.fromNamespaceAndPath(SMP_Mod.MOD_ID,path);

        Window window = Minecraft.getInstance().getWindow();

        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();
        int heartSize = 12;
        int x = (screenWidth/2-heartSize/2)-heartSize/2;
        int y = screenHeight-40-heartSize;

        guiGraphicsExtractor.blit(
                RenderPipelines.GUI_TEXTURED,
                imgLocation,
                x,
                y,
                0,
                0,
                heartSize,
                heartSize,
                12,
                12



        );

    }

    public static void drawLowerRightHeart(GuiGraphicsExtractor guiGraphicsExtractor, String path) {

        Identifier imgLocation = Identifier.fromNamespaceAndPath(SMP_Mod.MOD_ID, path);

        Window window = Minecraft.getInstance().getWindow();

        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();
        int heartSize = 12;
        int x = (screenWidth / 2 - heartSize / 2) + heartSize/2;
        int y = screenHeight - 40 - heartSize;

        guiGraphicsExtractor.blit(
                RenderPipelines.GUI_TEXTURED,
                imgLocation,
                x,
                y,
                0,
                0,
                heartSize,
                heartSize,
                12,
                12


        );
    }

}
