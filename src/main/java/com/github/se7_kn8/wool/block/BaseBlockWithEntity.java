package com.github.se7_kn8.wool.block;

import com.github.se7_kn8.wool.Wool;
import com.github.se7_kn8.wool.block.entity.BaseBlockEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sortme.ItemScatterer;
import net.minecraft.util.BlockHitResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BaseBlockWithEntity<T extends BaseBlockEntity> extends BlockWithEntity {
	private Supplier<T> blockEntitySupplier;

	public BaseBlockWithEntity(Supplier<T> blockEntitySupplier, Settings block$Settings_1) {
		super(block$Settings_1);
		this.blockEntitySupplier = blockEntitySupplier;
	}


	@Override
	public BlockRenderType getRenderType(BlockState blockState_1) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		return blockEntitySupplier.get();
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
		if (world.isClient) {
			return true;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BaseBlockEntity) {
				String containerId = ((BaseBlockEntity) blockEntity).getContainerId();
				if (containerId != null) {
					ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(Wool.MODID, ((BaseBlockEntity) blockEntity).getContainerId()), player, packetByteBuf -> packetByteBuf.writeBlockPos(pos));
				}
			}
			return true;
		}
	}

	@Override
	public void onBlockRemoved(BlockState oldBlockState, World world, BlockPos pos, BlockState newBlockState, boolean boolean_1) {
		if (oldBlockState.getBlock() != newBlockState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BaseBlockEntity) {
				ItemScatterer.spawn(world, pos, (BaseBlockEntity) blockEntity);
				world.updateHorizontalAdjacent(pos, this);
			}
			super.onBlockRemoved(oldBlockState, world, pos, newBlockState, boolean_1);
		}
	}
}
