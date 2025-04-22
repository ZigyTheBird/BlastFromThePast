package com.clonz.blastfromthepast.block;

import com.clonz.blastfromthepast.BlastFromThePast;
import com.clonz.blastfromthepast.block.signs.BFTPCeilingHangingSignBlock;
import com.clonz.blastfromthepast.block.signs.BFTPStandingSignBlock;
import com.clonz.blastfromthepast.block.signs.BFTPWallHangingSignBlock;
import com.clonz.blastfromthepast.block.signs.BFTPWallSignBlock;
import com.clonz.blastfromthepast.init.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BFTPWoodGroup {
    public final DeferredBlock<Block> BLOCK;
    public final DeferredBlock<SlabBlock> SLAB;
    public final DeferredBlock<StairBlock> STAIRS;
    public final DeferredBlock<FenceBlock> FENCE;
    public final DeferredBlock<FenceGateBlock> FENCE_GATE;
    public final DeferredBlock<RotatedPillarBlock> LOG;
    public final DeferredBlock<RotatedPillarBlock> WOOD;
    public final DeferredBlock<RotatedPillarBlock> STRIPPED_WOOD;
    public final DeferredBlock<RotatedPillarBlock> STRIPPED_LOG;
    public final DeferredBlock<DoorBlock> DOOR;
    public final DeferredBlock<ButtonBlock> BUTTON;
    public final DeferredBlock<PressurePlateBlock> PRESSURE_PLATE;
    public final DeferredBlock<TrapDoorBlock> TRAPDOOR;
    public final DeferredBlock<StandingSignBlock> SIGN;
    public final DeferredItem<Item> SIGN_ITEM;
    public final DeferredBlock<WallSignBlock> WALL_SIGN;
    public final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN;
    public final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_WALL;
    public final DeferredItem<Item> HANGING_SIGN_ITEM;
    public final DeferredBlock<Block> LEAVES;

    public final WoodType woodType;
    public final BlockSetType woodSetType;

    public BFTPWoodGroup(String name, MapColor color, Item.Properties empty, DeferredRegister.Blocks blockRegister) {
        woodSetType = BlockSetType.register(new BlockSetType(
                name,
                true,
                true,
                true,
                BlockSetType.PressurePlateSensitivity.EVERYTHING,
                SoundType.WOOD,
                SoundEvents.WOODEN_DOOR_CLOSE,
                SoundEvents.WOODEN_DOOR_OPEN,
                SoundEvents.WOODEN_TRAPDOOR_CLOSE,
                SoundEvents.WOODEN_TRAPDOOR_CLOSE,
                SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.WOODEN_BUTTON_CLICK_OFF,
                SoundEvents.WOODEN_BUTTON_CLICK_ON
        ));
        woodType = WoodType.register(new WoodType(BlastFromThePast.MODID + ":" + name, woodSetType));
        BLOCK = blockRegister.register(name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(color)));
        SLAB = blockRegister.register(name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).mapColor(color)));
        STAIRS = blockRegister.register(name + "_stairs", () -> new StairBlock(BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).mapColor(color)));
        FENCE = blockRegister.register(name + "_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).mapColor(color)));
        FENCE_GATE = blockRegister.register(name + "_fence_gate", () -> new FenceGateBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).mapColor(color)));
        STRIPPED_LOG = blockRegister.register("stripped_" + name + "_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).mapColor(color)));
        LOG = blockRegister.register(name + "_log", () -> new CustomLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(color), STRIPPED_LOG));
        STRIPPED_WOOD = blockRegister.register("stripped_" + name + "_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).mapColor(color)));
        WOOD = blockRegister.register(name + "_wood", () -> new CustomLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(color), STRIPPED_WOOD));
        DOOR = blockRegister.register(name + "_door", () -> new DoorBlock(woodSetType,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).mapColor(color)));
        BUTTON = blockRegister.register(name + "_button", () -> new ButtonBlock(woodSetType, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON).mapColor(color)));
        PRESSURE_PLATE = blockRegister.register(name + "_pressure_plate", () -> new PressurePlateBlock(woodSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).mapColor(color)));
        SIGN = blockRegister.register(name + "_sign", () -> new BFTPStandingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).mapColor(color), woodType));
        TRAPDOOR = blockRegister.register(name + "_trapdoor", () -> new TrapDoorBlock(woodSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).mapColor(color)));
        WALL_SIGN = blockRegister.register(name + "_wall_sign", () -> new BFTPWallSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).mapColor(color), woodType));
        HANGING_SIGN = blockRegister.register(name + "_hanging_sign", () -> new BFTPCeilingHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).mapColor(color), woodType));
        HANGING_SIGN_WALL = blockRegister.register(name + "_hanging_wall_sign", () -> new BFTPWallHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).mapColor(color), woodType));
        if (name.equals("cedar")) LEAVES = blockRegister.register("cedar_leaves", () -> new CedarLeavesBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.2F)
                        .randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .isValidSpawn((pState, pLevel, pPos, pValue) -> pValue == EntityType.OCELOT || pValue == EntityType.PARROT)
                        .isSuffocating(((pState, pLevel, pPos) -> false))
                        .isViewBlocking(((pState, pLevel, pPos) -> false))
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor(((pState, pLevel, pPos) -> false))
        ));
        else LEAVES = blockRegister.register(name + "_leaves", BFTPWoodGroup::leaves);

        ModItems.register(name, () -> new BlockItem(BLOCK.get(), empty));
        ModItems.register(name + "_slab", () -> new BlockItem(SLAB.get(), empty));
        ModItems.register(name + "_stairs", () -> new BlockItem(STAIRS.get(), empty));
        ModItems.register(name + "_fence", () -> new BlockItem(FENCE.get(), empty));
        ModItems.register(name + "_fence_gate", () -> new BlockItem(FENCE_GATE.get(), empty));
        ModItems.register("stripped_" + name + "_log", () -> new BlockItem(STRIPPED_LOG.get(), empty));
        ModItems.register(name + "_log", () -> new BlockItem(LOG.get(), empty));
        ModItems.register(name + "_wood", () -> new BlockItem(WOOD.get(), empty));
        ModItems.register("stripped_" + name + "_wood", () -> new BlockItem(STRIPPED_WOOD.get(), empty));
        ModItems.register(name + "_button", () -> new BlockItem(BUTTON.get(), empty));
        ModItems.register(name + "_pressure_plate", () -> new BlockItem(PRESSURE_PLATE.get(), empty));
        ModItems.register(name + "_trapdoor", () -> new BlockItem(TRAPDOOR.get(), empty));
        SIGN_ITEM = ModItems.register(name + "_sign", () -> new SignItem(empty, SIGN.get(), WALL_SIGN.get()));
        HANGING_SIGN_ITEM = ModItems.register(name + "_hanging_sign", () -> new HangingSignItem(HANGING_SIGN.get(), HANGING_SIGN_WALL.get(), empty));
        ModItems.register(name + "_door", () -> new DoubleHighBlockItem(DOOR.get(), empty));
        ModItems.register(name + "_leaves", () -> new BlockItem(LEAVES.get(), empty));
    }

    public static Block leaves() {
        return new LeavesBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.2F)
                        .randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .isValidSpawn((pState, pLevel, pPos, pValue) -> pValue == EntityType.OCELOT || pValue == EntityType.PARROT)
                        .isSuffocating(((pState, pLevel, pPos) -> false))
                        .isViewBlocking(((pState, pLevel, pPos) -> false))
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor(((pState, pLevel, pPos) -> false))
        );
    }
}
