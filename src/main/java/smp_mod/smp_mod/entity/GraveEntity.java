package smp_mod.smp_mod.entity;


import com.mojang.authlib.GameProfile;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class GraveEntity extends LivingEntity {

    public int xp;
    public SimpleContainer inventory = new SimpleContainer(54);
    public UUID killer;
    public UUID owner;
    public GameProfile gameProfile;


    public void addItemStack(ItemStack itemStack){


        this.inventory.addItem(itemStack);



    }

    public ItemStack getItemSlot(int slot){
        return this.inventory.getItem(slot);
    }



    public GraveEntity(EntityType<? extends GraveEntity> type, Level level) {

        super(type, level);
        this.xp = 0;
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,40)
                .add(Attributes.KNOCKBACK_RESISTANCE,1);
    }
    @Override
    public boolean fireImmune() {
        return true;
    }
    @Override
    public void tick(){
        super.tick();

        if(this.getY()<-60){
            this.setPos(this.getX(),-60,this.getZ());
            this.setNoGravity(true);
        }else if(this.isNoGravity()){
            this.setNoGravity(false);
        }
    }

    @Override
    public boolean isPushable(){
        return false;
    }


    public void doPush(){}




            @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }


    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        if(input.contains("xp")) {
            this.xp = input.getInt("xp").get().intValue();
        }
        if(input.contains("killerUUID")) {
            this.killer = UUID.fromString( input.getString("killerUUID").get());
        }
        if(input.contains("ownerUUID")) {
            this.owner = UUID.fromString( input.getString("ownerUUID").get());
        }

        if(input.contains("ownerName") && input.contains("ownerUUID")) {
            this.gameProfile = new GameProfile(UUID.fromString( input.getString("ownerUUID").get()), input.getString("ownerName").get());
        }

        ContainerHelper.loadAllItems(input,this.inventory.getItems());




    }

    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("xp", this.xp);

        if(this.killer != null) {
            output.putString("killerUUID", this.killer.toString());
        }
        if(this.owner != null) {
            output.putString("ownerUUID", this.owner.toString());
        }

        if(this.gameProfile != null){
            output.putString("ownerName",this.gameProfile.name() );
        }

        ContainerHelper.saveAllItems(output, this.inventory.getItems());
    }

    public int getXp(){
        return this.xp;
    }

    public void setXp(int amount){
        this.xp = amount;
    }

    @Override
    protected double getDefaultGravity(){
        return 0.08D;
    }

    public void setGameProfile(GameProfile gameProfile){
        this.gameProfile = gameProfile;
    }

    public GameProfile getGameProfile(){
        return this.gameProfile;
    }

}
