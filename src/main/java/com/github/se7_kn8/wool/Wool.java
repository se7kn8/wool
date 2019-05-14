package com.github.se7_kn8.wool;

import com.github.se7_kn8.wool.block.AlarmBlock;
import com.github.se7_kn8.wool.block.BaseBlockWithEntity;
import com.github.se7_kn8.wool.block.FanBlock;
import com.github.se7_kn8.wool.block.ItemCollectorBlock;
import com.github.se7_kn8.wool.block.entity.*;
import com.github.se7_kn8.wool.container.ItemCollectorContainer;
import com.github.se7_kn8.wool.container.MachineContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unsued") // Loaded by fabric-loader
public class Wool implements ModInitializer {
	public static final String MODID = "wool";

	public static ItemGroup WOOL_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(Wool.MODID, "default_group"), () -> new ItemStack(Blocks.WHITE_WOOL));

	private static final Map<Identifier, Item> ITEMS = new HashMap<>();
	private static final Map<Identifier, Block> BLOCKS = new HashMap<>();
	private static final Map<Identifier, SoundEvent> SOUND_EVENTS = new HashMap<>();
	private static final Map<Identifier, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITIES = new HashMap<>();

	public static final Block TREE_CUTTER = addBlock("tree_cutter", new BaseBlockWithEntity<>(TreeCutterBlockEntity::new, FabricBlockSettings.copy(Blocks.FURNACE).lightLevel(0).build()), WOOL_ITEM_GROUP);
	public static final Block SHEEP_SHAVER_BLOCK = addBlock("shearer", new BaseBlockWithEntity<>(ShearerBlockEntity::new, FabricBlockSettings.copy(Blocks.FURNACE).lightLevel(0).build()), WOOL_ITEM_GROUP);
	public static final Block WOOL_COLLECTOR_BLOCK = addBlock("wool_collector", new ItemCollectorBlock<>(WoolCollectorBlockEntity::new, FabricBlockSettings.copy(Blocks.FURNACE).lightLevel(0).build()), WOOL_ITEM_GROUP);
	public static final Block WOOD_COLLECTOR_BLOCK = addBlock("wood_collector", new ItemCollectorBlock<>(WoodCollectorBlockEntity::new, FabricBlockSettings.copy(Blocks.FURNACE).lightLevel(0).build()), WOOL_ITEM_GROUP);
	public static final Block FAN_BLOCK = addBlock("fan", new FanBlock(FanBlockEntity::new), WOOL_ITEM_GROUP);
	public static final Block ALARM_BLOCK = addBlock("alarm_block", new AlarmBlock(), WOOL_ITEM_GROUP);

	public static final BlockEntityType<TreeCutterBlockEntity> TREE_CUTTER_BLOCK_ENTITY = addBlockEntity("tree_cutter", BlockEntityType.Builder.create(TreeCutterBlockEntity::new, TREE_CUTTER));
	public static final BlockEntityType<ShearerBlockEntity> SHEEP_SHAVER_BLOCK_ENTITY = addBlockEntity("shearer", BlockEntityType.Builder.create(ShearerBlockEntity::new, SHEEP_SHAVER_BLOCK));
	public static final BlockEntityType<WoolCollectorBlockEntity> WOOL_COLLECTOR_BLOCK_ENTITY = addBlockEntity("wool_collector", BlockEntityType.Builder.create(WoolCollectorBlockEntity::new, WOOL_COLLECTOR_BLOCK));
	public static final BlockEntityType<WoodCollectorBlockEntity> WOOD_COLLECTOR_BLOCK_ENTITY = addBlockEntity("wood_collector", BlockEntityType.Builder.create(WoodCollectorBlockEntity::new, WOOD_COLLECTOR_BLOCK));
	public static final BlockEntityType<FanBlockEntity> FAN_BLOCK_ENTITY = addBlockEntity("fan", BlockEntityType.Builder.create(FanBlockEntity::new, FAN_BLOCK));

	public static final Identifier ADD_VELOCITY_TO_PLAYER_PACKET = new Identifier(Wool.MODID, "add_player_velocity");

	public static final SoundEvent ALARM_SOUND_EVENT = addSoundEvent("block.alarm.sound");

	@Override
	public void onInitialize() {

		for (Identifier identifier : BLOCKS.keySet()) {
			Registry.register(Registry.BLOCK, identifier, BLOCKS.get(identifier));
		}

		for (Identifier identifier : ITEMS.keySet()) {
			Registry.register(Registry.ITEM, identifier, ITEMS.get(identifier));
		}


		for (Identifier identifier : BLOCK_ENTITIES.keySet()) {
			Registry.register(Registry.BLOCK_ENTITY, identifier, BLOCK_ENTITIES.get(identifier));
		}

		for (Identifier identifier : SOUND_EVENTS.keySet()) {
			Registry.register(Registry.SOUND_EVENT, identifier, SOUND_EVENTS.get(identifier));
		}

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "shearer"), (syncId, identifier, player, buf) -> {
			BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			if (blockEntity instanceof ShearerBlockEntity) {
				return new MachineContainer(syncId, (ShearerBlockEntity) blockEntity, player.inventory) {
					@Override
					protected boolean isItemUsable(ItemStack stack) {
						return stack.getItem() instanceof ShearsItem;
					}

					@Override
					protected String getName() {
						return "shearer";
					}
				};
			}
			return null;
		});

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "tree_cutter"), (syncId, identifier, player, buf) -> {
			BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			if (blockEntity instanceof TreeCutterBlockEntity) {
				return new MachineContainer(syncId, (TreeCutterBlockEntity) blockEntity, player.inventory) {
					@Override
					protected boolean isItemUsable(ItemStack stack) {
						return stack.getItem() instanceof AxeItem;
					}

					@Override
					protected String getName() {
						return "tree_cutter";
					}
				};
			}

			return null;
		});

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "wool_collector"), (syncId, identifier, player, buf) -> {
			BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			if (blockEntity instanceof ItemCollectorBlockEntity) {
				return new ItemCollectorContainer(syncId, (ItemCollectorBlockEntity) blockEntity, player.inventory) {
					@Override
					protected String getName() {
						return "wool_collector";
					}
				};
			}
			return null;
		});

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "wood_collector"), (syncId, identifier, player, buf) -> {
			BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			if (blockEntity instanceof ItemCollectorBlockEntity) {
				return new ItemCollectorContainer(syncId, (ItemCollectorBlockEntity) blockEntity, player.inventory) {
					@Override
					protected String getName() {
						return "wood_collector";
					}
				};
			}
			return null;
		});
	}

	private static SoundEvent addSoundEvent(String name) {
		SoundEvent event = new SoundEvent(new Identifier(Wool.MODID, name));
		Wool.SOUND_EVENTS.put(new Identifier(Wool.MODID, name), event);
		return event;
	}

	private static <T extends BlockEntity> BlockEntityType<T> addBlockEntity(String name, BlockEntityType.Builder<T> blockEntityTypeBuilder) {
		BlockEntityType<T> blockEntityType = blockEntityTypeBuilder.build(null);
		Wool.BLOCK_ENTITIES.put(new Identifier(Wool.MODID, name), blockEntityType);
		return blockEntityType;
	}

	private static Block addBlock(String name, Block block, ItemGroup tab) {
		Wool.BLOCKS.put(new Identifier(Wool.MODID, name), block);
		BlockItem blockItem = new BlockItem(block, new Item.Settings().itemGroup(tab));
		blockItem.registerBlockItemMap(Item.BLOCK_ITEM_MAP, blockItem);
		Wool.ITEMS.put(new Identifier(Wool.MODID, name), blockItem);
		return block;
	}
}