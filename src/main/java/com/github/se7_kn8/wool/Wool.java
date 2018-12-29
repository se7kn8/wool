package com.github.se7_kn8.wool;

import com.github.se7_kn8.wool.block.BaseBlockWithEntity;
import com.github.se7_kn8.wool.block.ItemCollectorBlock;
import com.github.se7_kn8.wool.block.entity.ItemCollectorBlockEntity;
import com.github.se7_kn8.wool.block.entity.ShearerBlockEntity;
import com.github.se7_kn8.wool.block.entity.WoolCollectorBlockEntity;
import com.github.se7_kn8.wool.client.gui.BlockEntityInventoryGui;
import com.github.se7_kn8.wool.container.BlockEntityInventoryContainer;
import com.github.se7_kn8.wool.container.ItemCollectorContainer;
import com.github.se7_kn8.wool.container.ShearerContainer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.gui.GuiFactory;
import net.fabricmc.fabric.api.client.gui.GuiProviderRegistry;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;


public class Wool implements ModInitializer {
	public static final String MODID = "wool";

	public static ItemGroup WOOL_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(Wool.MODID, "default_group"), () -> new ItemStack(Blocks.WHITE_WOOL));

	private static final Map<Identifier, Item> ITEMS = new HashMap<>();
	private static final Map<Identifier, Block> BLOCKS = new HashMap<>();
	private static final Map<Identifier, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITIES = new HashMap<>();

	public static final Block SHEEP_SHAVER_BLOCK = addBlock("shearer", new BaseBlockWithEntity<>(ShearerBlockEntity::new, FabricBlockSettings.of(Material.STONE).build()), WOOL_ITEM_GROUP);
	public static final Block WOOL_COLLECTOR_BLOCK = addBlock("wool_collector", new ItemCollectorBlock<>(WoolCollectorBlockEntity::new), WOOL_ITEM_GROUP);

	public static final BlockEntityType<ShearerBlockEntity> SHEEP_SHAVER_BLOCK_ENTITY = addBlockEntity("shearer", BlockEntityType.Builder.create(ShearerBlockEntity::new));
	public static final BlockEntityType<WoolCollectorBlockEntity> WOOL_COLLECTOR_BLOCK_ENTITY = addBlockEntity("wool_collector", BlockEntityType.Builder.create(WoolCollectorBlockEntity::new));

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

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "shearer"), (identifier, player, buf) -> {
			BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			if (blockEntity instanceof ShearerBlockEntity) {
				return new ShearerContainer((ShearerBlockEntity) blockEntity, player.inventory);
			}
			return null;
		});

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "wool_collector"), (identifier, player, buf) -> {
			BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			if (blockEntity instanceof ItemCollectorBlockEntity) {
				return new ItemCollectorContainer((ItemCollectorBlockEntity) blockEntity, player.inventory);
			}
			return null;
		});

		GuiProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "shearer"), createBasicInventoryGui("textures/gui/container/shearer.png"));
		GuiProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "wool_collector"), createBasicInventoryGui(new Identifier("textures/gui/container/dispenser.png"))); // Use minecraft texture

	}

	private static <T extends BlockEntity> BlockEntityType<T> addBlockEntity(String name, BlockEntityType.Builder<T> blockEntityTypeBuilder) {
		BlockEntityType<T> blockEntityType = blockEntityTypeBuilder.method_11034(null);
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

	private <T extends BlockEntityInventoryContainer> GuiFactory<T> createBasicInventoryGui(Identifier identifier) {
		return container -> new BlockEntityInventoryGui(container) {
			@Override
			protected Identifier getBackground() {
				return identifier;
			}
		};
	}

	private <T extends BlockEntityInventoryContainer> GuiFactory<T> createBasicInventoryGui(String backgroundPath) {
		return createBasicInventoryGui(new Identifier(Wool.MODID, backgroundPath));
	}
}