package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, BlastFromThePast.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> AURORA_FROLICS = registerSoundEvent(BlastFromThePast.location("aurora_frolics"));

    // MUSIC DISC
    public static final DeferredHolder<SoundEvent, SoundEvent> BLIZZARD_REVELRY = registerSoundEvent(ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, "blizzard_revelry"));
    public static final ResourceKey<JukeboxSong> BLIZZARD_REVELRY_KEY = registerSong("blizzard_revelry");

    // GLACEROS
    public static final DeferredHolder<SoundEvent, SoundEvent> GLACEROS_DEATH = registerSoundEventForEntityType(ModEntities.GLACEROS, "_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> GLACEROS_HURT = registerSoundEventForEntityType(ModEntities.GLACEROS, "_hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> GLACEROS_IDLE = registerSoundEventForEntityType(ModEntities.GLACEROS, "_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLACEROS_SHEAR = registerSoundEventForEntityType(ModEntities.GLACEROS, "_shear");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLACEROS_HOOF_SCRAPE = registerSoundEventForEntityType(ModEntities.GLACEROS, "_hoof_scrape");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLACEROS_CLASH = registerSoundEventForEntityType(ModEntities.GLACEROS, "_clash");

    // SNOWDO
    public static final DeferredHolder<SoundEvent, SoundEvent> SNOWDO_DEATH = registerSoundEventForEntityType(ModEntities.SNOWDO, "_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> SNOWDO_HURT = registerSoundEventForEntityType(ModEntities.SNOWDO, "_hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> SNOWDO_IDLE = registerSoundEventForEntityType(ModEntities.SNOWDO, "_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNOWDO_TRIP = registerSoundEventForEntityType(ModEntities.SNOWDO, "_trip");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNOWDO_FLAP = registerSoundEventForEntityType(ModEntities.SNOWDO, "_flap");

    // FROSTOMPER
    public static final DeferredHolder<SoundEvent, SoundEvent> FROSTOMPER_CHARGE = registerSoundEventForEntityType(ModEntities.FROSTOMPER, "_charge");
    public static final DeferredHolder<SoundEvent, SoundEvent> FROSTOMPER_DEATH = registerSoundEventForEntityType(ModEntities.FROSTOMPER, "_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> FROSTOMPER_HURT = registerSoundEventForEntityType(ModEntities.FROSTOMPER, "_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> FROSTOMPER_IDLE = registerSoundEventForEntityType(ModEntities.FROSTOMPER, "_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> FROSTOMPER_STOMP = registerSoundEventForEntityType(ModEntities.FROSTOMPER, "_stomp");
    public static final DeferredHolder<SoundEvent, SoundEvent> FROSTOMPER_TRUMPET = registerSoundEventForEntityType(ModEntities.FROSTOMPER, "_trumpet");

    // PSYCHO BEAR
    public static final DeferredHolder<SoundEvent, SoundEvent> PSYCHO_BEAR_DEATH = registerSoundEventForEntityType(ModEntities.PSYCHO_BEAR, "_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> PSYCHO_BEAR_EAT = registerSoundEventForEntityType(ModEntities.PSYCHO_BEAR, "_eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> PSYCHO_BEAR_HURT = registerSoundEventForEntityType(ModEntities.PSYCHO_BEAR, "_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> PSYCHO_BEAR_IDLE = registerSoundEventForEntityType(ModEntities.PSYCHO_BEAR, "_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> PSYCHO_BEAR_ROAR = registerSoundEventForEntityType(ModEntities.PSYCHO_BEAR, "_roar");
    public static final DeferredHolder<SoundEvent, SoundEvent> PSYCHO_BEAR_SCRATCH = registerSoundEventForEntityType(ModEntities.PSYCHO_BEAR, "_scratch");
    public static final DeferredHolder<SoundEvent, SoundEvent> PSYCHO_BEAR_SLASH = registerSoundEventForEntityType(ModEntities.PSYCHO_BEAR, "_slash");
    public static final DeferredHolder<SoundEvent, SoundEvent> PSYCHO_BEAR_SNORE = registerSoundEventForEntityType(ModEntities.PSYCHO_BEAR, "_snore");

    public static final DeferredHolder<SoundEvent, SoundEvent> SPEARTOOTH_DEATH = registerSoundEventForEntityType(ModEntities.SPEARTOOTH, "_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPEARTOOTH_HURT = registerSoundEventForEntityType(ModEntities.SPEARTOOTH, "_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPEARTOOTH_IDLE = registerSoundEventForEntityType(ModEntities.SPEARTOOTH, "_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPEARTOOTH_ROAR = registerSoundEventForEntityType(ModEntities.SPEARTOOTH, "_roar");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPEARTOOTH_YAWN = registerSoundEventForEntityType(ModEntities.SPEARTOOTH, "_yawn");

    public static final DeferredHolder<SoundEvent, SoundEvent> BURREL_DEATH = registerSoundEventForEntityType(ModEntities.BURREL, "_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> BURREL_HURT = registerSoundEventForEntityType(ModEntities.BURREL, "_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> BURREL_IDLE = registerSoundEventForEntityType(ModEntities.BURREL, "_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> BURREL_EAT = registerSoundEventForEntityType(ModEntities.BURREL, "_eat");

    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_TRAP = registerSoundEvent(BlastFromThePast.location("bear_trap"));
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_GLOVE_SLASH = registerSoundEvent(BlastFromThePast.location("bear_glove_slash"));
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_GLOVE_SLASH_CRIT = registerSoundEvent(BlastFromThePast.location("bear_glove_slash_crit"));
    public static final DeferredHolder<SoundEvent, SoundEvent> WALL_GRAB = registerSoundEvent(BlastFromThePast.location("wall_grab"));

    private static ResourceKey<JukeboxSong> registerSong(String name) {
         return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(BlastFromThePast.MODID, name));
    }

    private static <T extends Entity> DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(ResourceLocation soundEventLocation) {
        return SOUND_EVENTS.register(
                soundEventLocation.getPath(),
                () -> SoundEvent.createFixedRangeEvent(soundEventLocation, 16f)
        );
    }

    private static <T extends Entity> DeferredHolder<SoundEvent, SoundEvent> registerSoundEventForEntityType(DeferredHolder<EntityType<?>, EntityType<T>> entityTypeHolder, String suffix) {
        ResourceLocation soundEventLocation = entityTypeHolder.getId().withSuffix(suffix);
        return SOUND_EVENTS.register(
                soundEventLocation.getPath(),
                () -> SoundEvent.createVariableRangeEvent(soundEventLocation)
        );
    }
}
