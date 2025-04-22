package com.clonz.blastfromthepast.init;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.block.AntlerDisplayBlockEntity;
import com.clonz.blastfromthepast.block.BearTrapBlockEntity;
import com.clonz.blastfromthepast.block.signs.entity.BFTPHangingSignBlockEntity;
import com.clonz.blastfromthepast.block.signs.entity.BFTPSignBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, BlastFromThePast.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BFTPHangingSignBlockEntity>> HANGING_SIGN = BLOCK_ENTITY_TYPES.register("hanging_sign", () -> BlockEntityType.Builder.of(BFTPHangingSignBlockEntity::new, ModBlocks.CEDAR.HANGING_SIGN.get(), ModBlocks.CEDAR.HANGING_SIGN_WALL.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BFTPSignBlockEntity>> SIGN = BLOCK_ENTITY_TYPES.register("sign", () -> BlockEntityType.Builder.of(BFTPSignBlockEntity::new, ModBlocks.CEDAR.SIGN.get(), ModBlocks.CEDAR.WALL_SIGN.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BearTrapBlockEntity>> BEAR_TRAP = BLOCK_ENTITY_TYPES.register("bear_trap", () -> BlockEntityType.Builder.of(BearTrapBlockEntity::new, ModBlocks.BEAR_TRAP.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AntlerDisplayBlockEntity>> ANTLER_DISPLAY = BLOCK_ENTITY_TYPES.register("antler_display", () -> BlockEntityType.Builder.of(AntlerDisplayBlockEntity::new, ModBlocks.ANTLER_DISPLAY.get(), ModBlocks.BROAD_ANTLER_DISPLAY.get(), ModBlocks.CURLY_ANTLER_DISPLAY.get(), ModBlocks.SPIKEY_ANTLER_DISPLAY.get()).build(null));
}
