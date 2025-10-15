package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.entity.*;
import team.recrafted.blastfromthepast.entity.boats.BFTPBoat;
import team.recrafted.blastfromthepast.entity.boats.BFTPChestBoat;
import team.recrafted.blastfromthepast.entity.projectile.ThrownIceSpear;
import team.recrafted.blastfromthepast.entity.speartooth.SpeartoothEntity;
import team.recrafted.blastfromthepast.util.HitboxHelper;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, BlastFromThePast.MODID);

    public static <T extends Mob> RegistryObject<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> entity,
                                                                                           float width, float height) {
        RegistryObject<EntityType<T>> entityType = ENTITIES.register(name,
                () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));

        return entityType;
    }

    public static <T extends Mob> RegistryObject<EntityType<T>> registerMobWithEyeHeight(String name, EntityType.EntityFactory<T> entity,
                                                                                                        float width, float height) {
        return ENTITIES.register(name,
                () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));
    }

    public static final RegistryObject<EntityType<GlacerosEntity>> GLACEROS = registerMob("glaceros", GlacerosEntity::new,
            1f, 2.2f);

    public static final RegistryObject<EntityType<SnowdoEntity>> SNOWDO = registerMob("snowdo", SnowdoEntity::new,
            0.6f, 1.1f);

    public static final RegistryObject<EntityType<FrostomperEntity>> FROSTOMPER = registerMobWithEyeHeight("frostomper", FrostomperEntity::new,
            4, HitboxHelper.pixelsToBlocks(70.0F));

    public static final RegistryObject<EntityType<SpeartoothEntity>> SPEARTOOTH = registerMobWithEyeHeight("speartooth", SpeartoothEntity::new
            , 0.9F, 1.3F);

    public static final RegistryObject<EntityType<BurrelEntity>> BURREL = registerMob("burrel", BurrelEntity::new
            , 1.0F, 1.0F);

    public static final RegistryObject<EntityType<ThrownIceSpear>> ICE_SPEAR = ENTITIES.register("ice_spear",
            () -> EntityType.Builder.<ThrownIceSpear>of(ThrownIceSpear::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("ice_spear"));

    public static final RegistryObject<EntityType<SapEntity>> SAP = ENTITIES.register("sap", () -> EntityType.Builder
            .<SapEntity>of(SapEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10)
            .updateInterval(Integer.MAX_VALUE)
            .build(getName("sap")));

    public static final RegistryObject<EntityType<BFTPBoat>> BFTPBOAT = ENTITIES.register("boat", () -> EntityType.Builder
            .<BFTPBoat>of(BFTPBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
            .build(getName("boat")));

    public static final RegistryObject<EntityType<BFTPChestBoat>> BFTPCHEST_BOAT = ENTITIES.register("chest_boat", () -> EntityType.Builder
            .<BFTPChestBoat>of(BFTPChestBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
            .build(getName("chest_boat")));

    public static final RegistryObject<EntityType<PsychoBearEntity>> PSYCHO_BEAR = registerMob("psycho_bear", PsychoBearEntity::new,
            HitboxHelper.pixelsToBlocks(53.0F), HitboxHelper.pixelsToBlocks(33.0F));

    public static final RegistryObject<EntityType<HollowEntity>> HOLLOW = ENTITIES.register("hollow", () -> EntityType.Builder
            .of(HollowEntity::new, MobCategory.MISC)
            .sized(1.3F, 2.3F)
            .clientTrackingRange(10)
            .build(getName("hollow")));

    public static final RegistryObject<EntityType<TarArrow>> TAR_ARROW = ENTITIES.register("tar_arrow", () -> EntityType.Builder
            .<TarArrow>of(TarArrow::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .build(getName("tar_arrow")));

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GLACEROS.get(), GlacerosEntity.createAttributes().build());
        event.put(ModEntities.SNOWDO.get(), SnowdoEntity.createAttributes().build());
        event.put(ModEntities.SPEARTOOTH.get(), SpeartoothEntity.createAttributes().build());
        event.put(ModEntities.BURREL.get(), BurrelEntity.createAttributes().build());
        event.put(ModEntities.FROSTOMPER.get(), FrostomperEntity.createAttributes().build());
        event.put(ModEntities.PSYCHO_BEAR.get(), PsychoBearEntity.createAttributes().build());
        event.put(ModEntities.HOLLOW.get(), HollowEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 200F).build());
    }

    private static String getName(String name) {
        return BlastFromThePast.MODID + ":" + name;
    }
}