package team.recrafted.blastfromthepast.events;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.BearTrapBlockEntity;
import team.recrafted.blastfromthepast.entity.GlacerosEntity;
import team.recrafted.blastfromthepast.entity.HollowEntity;
import team.recrafted.blastfromthepast.entity.TarArrow;
import team.recrafted.blastfromthepast.init.ModBiomes;
import team.recrafted.blastfromthepast.init.ModBlocks;
import team.recrafted.blastfromthepast.init.ModMobEffects;
import team.recrafted.blastfromthepast.init.ModSounds;
import team.recrafted.blastfromthepast.network.PsychoedEffectPayload;
import team.recrafted.blastfromthepast.util.EntityHelper;

import java.util.function.Function;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = BlastFromThePast.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    public static Predicate<BlockState> PERMAFROST_PREDICATE = state -> {
        Block block = state.getBlock();
        if (block == Blocks.STONE) return true;
        if (state.isAir()) return false;
        return block == Blocks.DIORITE || block == Blocks.ANDESITE || block == Blocks.GRANITE ||block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE || block == Blocks.DIAMOND_ORE
                || block == Blocks.EMERALD_ORE || block == Blocks.GOLD_ORE || block == Blocks.IRON_ORE
                || block == Blocks.LAPIS_ORE || block == Blocks.REDSTONE_ORE;
    };

    public static Function<BlockState, BlockState> PERMAFROST_FUNCTION = state -> {
        Block block = state.getBlock();
        if (block == Blocks.STONE) return ModBlocks.PERMAFROST.BLOCK.get().defaultBlockState();
        ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block);
        if (block == Blocks.GRANITE) return Blocks.ICE.defaultBlockState();
        if (block == Blocks.DIORITE) return Blocks.PACKED_ICE.defaultBlockState();
        if (block == Blocks.ANDESITE) return Blocks.BLUE_ICE.defaultBlockState();
        Block newBlock = BuiltInRegistries.BLOCK.get(BlastFromThePast.location("permafrost_" + location.getPath()));
        if (newBlock != Blocks.AIR) return newBlock.defaultBlockState();
        return state;
    };

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load event) {
        if (!event.isNewChunk()) return;
//        if (event.getChunk().getNoiseBiome(0, 0, 0).is(ModBiomes.FROSTBITE_RIVER) || event.getChunk().getNoiseBiome(0, 0, 0).is(ModBiomes.FROSTBITE_FOREST)) {
//            event.getChunk().findBlocks(PERMAFROST_PREDICATE, (pos, state) -> {
//                event.getLevel().setBlock(pos, PERMAFROST_FUNCTION.apply(state), 4, 0);
//            });
//        }
    }

    @SubscribeEvent
    public static void playerInteractEvent(PlayerInteractEvent.EntityInteract event){
        if(event.getTarget() instanceof GlacerosEntity glaceros){
            if(event.getItemStack().is(Items.SHEARS) && !glaceros.isBaby() && !glaceros.isSheared()){
                glaceros.setSheared(true);
                glaceros.antlerGrowCooldown = 1000 + glaceros.getRandom().nextInt(300);
                if(!event.getEntity().isCreative()){
                    event.getEntity().getItemInHand(event.getHand()).setDamageValue(1);
                }
                event.getLevel().playSound(event.getTarget(), event.getPos(), ModSounds.GLACEROS_SHEAR.get(), SoundSource.PLAYERS, 1 ,1);
                event.getEntity().swing(event.getHand());
                ItemStack antlers = new ItemStack(glaceros.getVariant().getAntlerItem(), 2);
                event.getLevel().addFreshEntity
                        (new ItemEntity(event.getLevel(), glaceros.getX(), glaceros.getY() + 0.5, glaceros.getZ(), antlers));

            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.hasEffect(ModMobEffects.PSYCHOD.get())) {
                BlastFromThePast.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new PsychoedEffectPayload(false));
            }
            if (EntityHelper.shouldCreateHollow(player)) {
                ItemStack idol = EntityHelper.getIdolOfRetrievalInHand(player);
                if (idol != null) {
                    CriteriaTriggers.CONSUME_ITEM.trigger(player, idol);
                    idol.shrink(1);
                }
                HollowEntity hollow = HollowEntity.create(player);
                player.serverLevel().addFreshEntity(hollow);
            }
        }
    }

    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onArrowHit(ProjectileImpactEvent event) {
        if (event.getRayTraceResult().getType() == HitResult.Type.BLOCK && event.getProjectile() instanceof TarArrow) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getHitVec().getBlockPos();
        Level level = event.getLevel();
        if (level.getBlockEntity(pos) instanceof BearTrapBlockEntity blockEntity) {
            blockEntity.interact(level, pos, event.getEntity());
        }
    }

    @SubscribeEvent
    public static void effectAdded(MobEffectEvent.Added event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getEffectInstance().getEffect().equals(ModMobEffects.PSYCHOD.get())) {
            BlastFromThePast.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new PsychoedEffectPayload(true));
        }
    }

    @SubscribeEvent
    public static void effectRemoved(MobEffectEvent.Remove event) {
        if(event.getEffectInstance()!=null){
            if (event.getEntity() instanceof ServerPlayer player && event.getEffectInstance().getEffect().equals(ModMobEffects.PSYCHOD.get())) {
                BlastFromThePast.INSTANCE.send(PacketDistributor.PLAYER.with( ()-> (ServerPlayer) event.getEntity()), new PsychoedEffectPayload(false));
            }
        }
    }

    @SubscribeEvent
    public static void effectExpired(MobEffectEvent.Expired event) {
        if(event.getEffectInstance()!=null){
            if (event.getEntity() instanceof ServerPlayer player && event.getEffectInstance().getEffect().equals(ModMobEffects.PSYCHOD.get())) {
                BlastFromThePast.INSTANCE.send(PacketDistributor.PLAYER.with( ()-> (ServerPlayer) event.getEntity()), new PsychoedEffectPayload(false));
            }
        }
    }

    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().hasEffect(ModMobEffects.PSYCHOD.get())) {
            BlastFromThePast.INSTANCE.send(PacketDistributor.PLAYER.with( ()-> (ServerPlayer) event.getEntity()), new PsychoedEffectPayload(true));
        }
    }
}