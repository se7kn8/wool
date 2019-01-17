package com.github.se7_kn8.wool;

import com.github.se7_kn8.wool.block.BaseBlockWithEntity;
import com.github.se7_kn8.wool.block.ItemCollectorBlock;
import com.github.se7_kn8.wool.block.entity.FanBlockEntity;
import com.github.se7_kn8.wool.block.entity.ItemCollectorBlockEntity;
import com.github.se7_kn8.wool.block.entity.ShearerBlockEntity;
import com.github.se7_kn8.wool.block.entity.WoolCollectorBlockEntity;
import com.github.se7_kn8.wool.container.ItemCollectorContainer;
import com.github.se7_kn8.wool.container.ShearerContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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


@SuppressWarnings("unsued") // Loaded by fabric-loader
public class Wool implements ModInitializer {
	public static final String MODID = "wool";

	public static ItemGroup WOOL_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(Wool.MODID, "default_group"), () -> new ItemStack(Blocks.WHITE_WOOL));

	private static final Map<Identifier, Item> ITEMS = new HashMap<>();
	private static final Map<Identifier, Block> BLOCKS = new HashMap<>();
	private static final Map<Identifier, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITIES = new HashMap<>();

	public static final Block SHEEP_SHAVER_BLOCK = addBlock("shearer", new BaseBlockWithEntity<>(ShearerBlockEntity::new, FabricBlockSettings.copy(Blocks.FURNACE).build()), WOOL_ITEM_GROUP);
	public static final Block WOOL_COLLECTOR_BLOCK = addBlock("wool_collector", new ItemCollectorBlock<>(WoolCollectorBlockEntity::new, FabricBlockSettings.copy(Blocks.FURNACE).build()), WOOL_ITEM_GROUP);
	public static final Block FAN_BLOCK/*TODO = addBlock("fan", new FanBlock(FanBlockEntity::new), WOOL_ITEM_GROUP)*/ = null;

	public static final BlockEntityType<ShearerBlockEntity> SHEEP_SHAVER_BLOCK_ENTITY = addBlockEntity("shearer", BlockEntityType.Builder.create(ShearerBlockEntity::new));
	public static final BlockEntityType<WoolCollectorBlockEntity> WOOL_COLLECTOR_BLOCK_ENTITY = addBlockEntity("wool_collector", BlockEntityType.Builder.create(WoolCollectorBlockEntity::new));
	public static final BlockEntityType<FanBlockEntity> FAN_BLOCK_ENTITY/*TODO = addBlockEntity("fan", BlockEntityType.Builder.create(FanBlockEntity::new))*/ = null;

	public static final Identifier ADD_VELOCITY_TO_PLAYER/*TODO = new Identifier(Wool.MODID, "add_player_velocity")*/ = null;

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

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "shearer"), (syncId, identifier, player, buf) -> {
			BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			if (blockEntity instanceof ShearerBlockEntity) {
				return new ShearerContainer(syncId, (ShearerBlockEntity) blockEntity, player.inventory);
			}
			return null;
		});

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "wool_collector"), (syncId, identifier, player, buf) -> {
			BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			if (blockEntity instanceof ItemCollectorBlockEntity) {
				return new ItemCollectorContainer(syncId, (ItemCollectorBlockEntity) blockEntity, player.inventory);
			}
			return null;
		});

		/*CustomPayloadPacketRegistry.CLIENT.register(ADD_VELOCITY_TO_PLAYER, (packetContext, packetByteBuf) -> {
			double velX = packetByteBuf.readDouble();
			double velY = packetByteBuf.readDouble();
			double velZ = packetByteBuf.readDouble();

			if (packetContext.getPlayer() != null && !packetContext.getPlayer().abilities.flying) {
				packetContext.getPlayer().addVelocity(velX, velY, velZ);
				packetContext.getPlayer().fallDistance = 0;
			}
		});*/

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