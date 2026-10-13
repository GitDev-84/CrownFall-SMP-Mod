package smp_mod.smp_mod.SaveData;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.filefix.AbortedFileFixException;

import java.net.URL;
import java.util.*;

public class ServerData {

    public List<String> playersToKill;
    public Map<String,String> playersAndDummys;
    public Map<String,String> carriedAndCarryers;
    public List<String> blockCarriers;
    public String webHookURL;
    public boolean allowTotems;


    public ServerData(){

        this.playersToKill = new ArrayList<>();
        this.playersAndDummys = new HashMap<>();
        this.carriedAndCarryers = new HashMap<>();
        this.blockCarriers = new ArrayList<>();
        this.webHookURL = "";
        this.allowTotems = true;

    }
    public ServerData(List<String> playersToKill, Map<String,String> playersAndDummys, Map<String, String> carriedAndCarryers, List<String> blockCarriers, String webHookURL, boolean allowTotems){

        this.playersToKill = new ArrayList<>(playersToKill);
        this.playersAndDummys = new HashMap<>(playersAndDummys);
        this.carriedAndCarryers = new HashMap<>(carriedAndCarryers);
        this.blockCarriers = new ArrayList<>( blockCarriers);
        this.webHookURL = webHookURL;
        this.allowTotems = allowTotems;


    }



    public static final Codec<ServerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                     Codec.STRING.listOf().fieldOf("players_to_kill").forGetter(d -> d.playersToKill),
                     Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("players_and_dummys").forGetter(d -> d.playersAndDummys),
                     Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("carriedAndCarryers").forGetter(d -> d.carriedAndCarryers),
                    Codec.STRING.listOf().fieldOf("blockCarriers").forGetter(d-> d.blockCarriers),
                    Codec.STRING.fieldOf("webHookURL").forGetter(d->d.webHookURL),
                    Codec.BOOL.fieldOf("allowTotems").forGetter(d->d.allowTotems)

            ).apply(instance, ServerData::new)
    );


}
