package com.github.se7_kn8.wool.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

import java.util.function.Supplier;

public class FanBlock extends BaseBlockWithEntity {
	public static final DirectionProperty DIRECTION;
	public static final BooleanProperty ACTIVE;

	static {
		DIRECTION = Properties.FACING;
		ACTIVE = BooleanProperty.create("active");
	}

	public FanBlock(Supplier blockEntitySupplier) {
		super(blockEntitySupplier, FabricBlockSettings.of(Material.STONE).build());
		this.setDefaultState(this.stateFactory.getDefaultState().with(DIRECTION, Direction.NORTH).with(ACTIVE, false));
	}

	@Override
	public BlockState rotate(BlockState blockState, BlockRotation rotation) {
		return blockState.with(DIRECTION, rotation.rotate(blockState.get(DIRECTION)));
	}

	@Override
	public BlockState mirror(BlockState blockState, BlockMirror mirror) {
		return blockState.rotate(mirror.getRotation(blockState.get(DIRECTION)));
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.with(DIRECTION, ACTIVE);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(DIRECTION, context.getPlayerFacing().getOpposite());
	}
}
