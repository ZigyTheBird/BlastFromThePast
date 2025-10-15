package team.recrafted.blastfromthepast.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import team.recrafted.blastfromthepast.BlastFromThePast;
import team.recrafted.blastfromthepast.block.*;
import team.recrafted.blastfromthepast.item.AntlerDisplayItem;
import team.recrafted.blastfromthepast.item.BearTrapBlockItem;
import team.recrafted.blastfromthepast.misc.AntlerDisplayType;

import java.util.function.Supplier;

public class ModBlocks {
    public static DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, BlastFromThePast.MODID);

    public static final BFTPWoodGroup CEDAR = new BFTPWoodGroup("cedar",  MapColor.COLOR_BROWN, new Item.Properties(), BLOCKS);
    public static final BFTPStoneGroup PERMAFROST = new BFTPStoneGroup("permafrost",  MapColor.STONE, new Item.Properties());

    public static final RegistryObject<Block> PINECONE = createRegistry("pinecone",
            () -> new PineconeBlock(ModTreeGrowers.CEDAR, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)), new Item.Properties());

    public static final RegistryObject<Block> SHAGGY_BLOCK = createRegistry("shaggy_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)), new Item.Properties());
    public static final RegistryObject<Block> BEASTLY_FEMUR = createRegistry("beastly_femur",
            () -> new FemurBlock(BlockBehaviour.Properties.copy(Blocks.BONE_BLOCK).noOcclusion()), new Item.Properties());
    public static final RegistryObject<Block> PSYCHO_BERRY_BUSH = createRegistry("psycho_berry_bush",
            () -> new PsychoBerryBush(BlockBehaviour.Properties.copy(Blocks.SPRUCE_LEAVES).noCollission()), new Item.Properties());
    public static final RegistryObject<Block> PSYCHO_BERRY_SPROUT = createRegistry("psycho_berry_sprout",
            () -> new PsychoBerrySprout(BlockBehaviour.Properties.copy(Blocks.DANDELION).randomTicks()), null);
    public static final RegistryObject<CustomLogBlock> SAPPY_CEDAR_LOG = createRegistry("sappy_cedar_log",
            () -> new CustomLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG), CEDAR.LOG), new Item.Properties());

    public static final RegistryObject<Block> ROYAL_LARKSPUR = createRegistry("royal_larkspur",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)), new Item.Properties());
    public static final RegistryObject<Block> BLUSH_LARKSPUR = createRegistry("blush_larkspur",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)), new Item.Properties());
    public static final RegistryObject<Block> SNOW_LARKSPUR = createRegistry("snow_larkspur",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)), new Item.Properties());
    public static final RegistryObject<Block> SHIVER_LARKSPUR = createRegistry("shiver_larkspur",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)), new Item.Properties());

    public static final RegistryObject<Block> SILENE = createRegistry("silene",
            () -> new FlowerBlock(()-> MobEffects.UNLUCK, 0, BlockBehaviour.Properties.copy(Blocks.SUNFLOWER)), new Item.Properties());

    public static final RegistryObject<Block> CHILLY_MOSS_SPROUT = createRegistry("chilly_moss_sprout",
            () -> new ChillyMossSprout(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).randomTicks().noOcclusion().noCollission()), new Item.Properties());

    public static final RegistryObject<Block> CHILLY_MOSS = createRegistry("chilly_moss",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).pushReaction(PushReaction.NORMAL)), new Item.Properties());

    public static final RegistryObject<Block> BEAST_CHOPS = createRegistry("raw_beast_chops",
            () -> new BeastChopsBlock(BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)), new Item.Properties());
    public static final RegistryObject<Block> BEAST_CHOPS_COOKED = createRegistry("cooked_beast_chops",
            () -> new BeastChopsBlock(BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)).setLevelAndSaturation(8, 12F), new Item.Properties());
    public static final RegistryObject<Block> BEAST_CHOPS_GLAZED = createRegistry("glazed_beast_chops",
            () -> new BeastChopsBlock(BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)).setLevelAndSaturation(8, 20F), new Item.Properties());

    public static final RegistryObject<Block> SNOWDO_EGG = createRegistry("snowdo_egg",
            () -> new SnowdoEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG).noOcclusion()), new Item.Properties());

    public static final RegistryObject<Block> BEAR_TRAP = createRegistry("bear_trap",
            () -> new BearTrapBlock(BlockBehaviour.Properties.copy(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE).noOcclusion()), new Item.Properties());

    public static final RegistryObject<Block> ANTLER_DISPLAY = createRegistry("antler_display",
            () -> new AntlerDisplayBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS).noOcclusion(), AntlerDisplayType.NORMAL), new Item.Properties());

    public static final RegistryObject<Block> BROAD_ANTLER_DISPLAY = createRegistry("broad_antler_display",
            () -> new AntlerDisplayBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS).noOcclusion(), AntlerDisplayType.BROAD), new Item.Properties());

    public static final RegistryObject<Block> SPIKEY_ANTLER_DISPLAY = createRegistry("spikey_antler_display",
            () -> new AntlerDisplayBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS).noOcclusion(), AntlerDisplayType.SPIKEY), new Item.Properties());

    public static final RegistryObject<Block> CURLY_ANTLER_DISPLAY = createRegistry("curly_antler_display",
            () -> new AntlerDisplayBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS).noOcclusion(), AntlerDisplayType.CURLY), new Item.Properties());

    public static final RegistryObject<Block> TAR = createRegistry("tar",
            () -> new TarBlock(BlockBehaviour.Properties.copy(Blocks.POWDER_SNOW).mapColor(MapColor.COLOR_BLACK).strength(4F).sound(SoundType.SLIME_BLOCK)), new Item.Properties());


    public static final RegistryObject<Block> PERMAFROST_BURREL_PAINTING = createRegistry("permafrost_burrel_painting",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());

    public static final RegistryObject<Block> PERMAFROST_SNOWDO_PAINTING = createRegistry("permafrost_snowdo_painting",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());

    public static final RegistryObject<Block> PERMAFROST_GLACEROS_PAINTING = createRegistry("permafrost_glaceros_painting",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());

    public static final RegistryObject<Block> PERMAFROST_SPEARTOOTH_PAINTING = createRegistry("permafrost_speartooth_painting",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());

    public static final RegistryObject<Block> PERMAFROST_PSYCHO_BEAR_PAINTING = createRegistry("permafrost_psycho_bear_painting",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());

    public static final RegistryObject<Block> PERMAFROST_FROSTOMPER_PAINTING_TOP_RIGHT = createRegistry("permafrost_frostomper_painting_top_right",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());

    public static final RegistryObject<Block> PERMAFROST_FROSTOMPER_PAINTING_TOP_LEFT = createRegistry("permafrost_frostomper_painting_top_left",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());

    public static final RegistryObject<Block> PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_RIGHT = createRegistry("permafrost_frostomper_painting_bottom_left",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());

    public static final RegistryObject<Block> PERMAFROST_FROSTOMPER_PAINTING_BOTTOM_LEFT = createRegistry("permafrost_frostomper_painting_bottom_right",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.STONE), false), new Item.Properties());


    public static final RegistryObject<Block> BURREL_TOTEM_POLE = createRegistry("burrel_totem_pole",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion(), true), new Item.Properties());

    public static final RegistryObject<Block> SNOWDO_TOTEM_POLE = createRegistry("snowdo_totem_pole",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion(), true), new Item.Properties());

    public static final RegistryObject<Block> GLACEROS_TOTEM_POLE = createRegistry("glaceros_totem_pole",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion(), true), new Item.Properties());

    public static final RegistryObject<Block> SPEARTOOTH_TOTEM_POLE = createRegistry("speartooth_totem_pole",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion(), true), new Item.Properties());

    public static final RegistryObject<Block> PSYCHO_BEAR_TOTEM_POLE = createRegistry("psycho_bear_totem_pole",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion(), true), new Item.Properties());

    public static final RegistryObject<Block> FROSTOMPER_TOTEM_POLE = createRegistry("frostomper_totem_pole",
            () -> new BlockWithDirection(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion(), true), new Item.Properties());

    public static final BFTPBlockGroup SNOW_BRICK = new BFTPBlockGroup("snow_brick", MapColor.SNOW, BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK), new Item.Properties());
    public static final BFTPBlockGroup ICE_BRICK = new BFTPBlockGroup("ice_brick", MapColor.ICE, BlockBehaviour.Properties.copy(Blocks.ICE), new Item.Properties());

    public static <T extends Block> RegistryObject<T> createRegistry(String name, Supplier<T> block, Item.Properties properties) {
        RegistryObject<T> object = BLOCKS.register(name, block);
        if (name.equals("bear_trap")) ModItems.ITEMS.register(name, () -> new BearTrapBlockItem(object.get(), properties));
        else if (name.endsWith("antler_display")) ModItems.ITEMS.register(name, () -> new AntlerDisplayItem(object.get(), properties));
        else if (properties != null) ModItems.ITEMS.register(name, () -> new BlockItem(object.get(), properties));

        return object;
    }
}
