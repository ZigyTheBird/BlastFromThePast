package team.recrafted.blastfromthepast.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.signs.BFTPCeilingHangingSignBlock;
import team.recrafted.blastfromthepast.block.signs.BFTPStandingSignBlock;
import team.recrafted.blastfromthepast.block.signs.BFTPWallHangingSignBlock;
import team.recrafted.blastfromthepast.block.signs.BFTPWallSignBlock;
import team.recrafted.blastfromthepast.init.ModItems;

public class BFTPWoodGroup {
    public final RegistryObject<Block> BLOCK;
    public final RegistryObject<SlabBlock> SLAB;
    public final RegistryObject<StairBlock> STAIRS;
    public final RegistryObject<FenceBlock> FENCE;
    public final RegistryObject<FenceGateBlock> FENCE_GATE;
    public final RegistryObject<RotatedPillarBlock> LOG;
    public final RegistryObject<RotatedPillarBlock> WOOD;
    public final RegistryObject<RotatedPillarBlock> STRIPPED_WOOD;
    public final RegistryObject<RotatedPillarBlock> STRIPPED_LOG;
    public final RegistryObject<DoorBlock> DOOR;
    public final RegistryObject<ButtonBlock> BUTTON;
    public final RegistryObject<PressurePlateBlock> PRESSURE_PLATE;
    public final RegistryObject<TrapDoorBlock> TRAPDOOR;
    public final RegistryObject<StandingSignBlock> SIGN;
    public final RegistryObject<WallSignBlock> WALL_SIGN;
    public final RegistryObject<CeilingHangingSignBlock> HANGING_SIGN;
    public final RegistryObject<WallHangingSignBlock> HANGING_SIGN_WALL;
    public final RegistryObject<Block> LEAVES;

    public final RegistryObject<Item> SIGN_ITEM;
    public final RegistryObject<Item> HANGING_SIGN_ITEM;

    public final WoodType woodType;
    public final BlockSetType woodSetType;

    public BFTPWoodGroup(String name, MapColor color, Item.Properties empty, DeferredRegister<Block> blockRegister) {
        woodSetType = BlockSetType.register(new BlockSetType(
                name,
                true,
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

        woodType = WoodType.register(new WoodType(BlastFromThePast.MOD_ID + ":" + name, woodSetType));
        //Block Registry
        BLOCK = blockRegister.register(name, () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).mapColor(color)));
        SLAB = blockRegister.register(name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB).mapColor(color)));
        STAIRS = blockRegister.register(name + "_stairs", () -> new StairBlock(()-> BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS).mapColor(color)));
        FENCE = blockRegister.register(name + "_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE).mapColor(color)));
        FENCE_GATE = blockRegister.register(name + "_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE).mapColor(color), woodType));
        STRIPPED_LOG = blockRegister.register("stripped_" + name + "_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).mapColor(color)));
        if(name.equals("cedar")){
            LOG = blockRegister.register(name + "_log", () -> new CedarLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).mapColor(color), STRIPPED_LOG));
        } else LOG = blockRegister.register(name + "_log", () -> new CustomLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).mapColor(color), STRIPPED_LOG));
        STRIPPED_WOOD = blockRegister.register("stripped_" + name + "_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).mapColor(color)));
        WOOD = blockRegister.register(name + "_wood", () -> new CustomLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).mapColor(color), STRIPPED_WOOD));
        DOOR = blockRegister.register(name + "_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).mapColor(color), woodSetType));
        BUTTON = blockRegister.register(name + "_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).mapColor(color), woodSetType, 30, true));
        PRESSURE_PLATE = blockRegister.register(name + "_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE).mapColor(color), woodSetType));
        SIGN = blockRegister.register(name + "_sign", () -> new BFTPStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN).mapColor(color), woodType));
        TRAPDOOR = blockRegister.register(name + "_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR).mapColor(color), woodSetType));
        WALL_SIGN = blockRegister.register(name + "_wall_sign", () -> new BFTPWallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).mapColor(color), woodType));
        HANGING_SIGN = blockRegister.register(name + "_hanging_sign", () -> new BFTPCeilingHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN).mapColor(color), woodType));
        HANGING_SIGN_WALL = blockRegister.register(name + "_hanging_wall_sign", () -> new BFTPWallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN).mapColor(color), woodType));

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

        //Items registry
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
