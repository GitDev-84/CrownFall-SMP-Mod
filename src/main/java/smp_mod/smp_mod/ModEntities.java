package smp_mod.smp_mod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import smp_mod.smp_mod.entity.DummyEntity;
import smp_mod.smp_mod.entity.GraveEntity;


public class ModEntities {

    public static final EntityType<GraveEntity> GRAVE =  Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("smp_mod", "grave"),
            EntityType.Builder
                    .of(GraveEntity::new, MobCategory.MISC)
                    .sized(2F,1.5F)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath("smp_mod","grave")
                    ))
    );

    public static final EntityType<DummyEntity> DUMMY =  Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("smp_mod", "dummy_entity"),
            EntityType.Builder
                    .of(DummyEntity::new, MobCategory.MISC)
                    .sized(0.6F,1.8F)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath("smp_mod","dummy_entity")
                    ))
    );

    public static void register(){

    }



}
