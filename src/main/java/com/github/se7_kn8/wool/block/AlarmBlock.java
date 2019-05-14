package com.github.se7_kn8.wool.block;

import com.github.se7_kn8.wool.Wool;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlarmBlock extends Block {

	public static final BooleanProperty POWERED = Properties.POWERED;

	public AlarmBlock() {
		super(FabricBlockSettings.copy(Blocks.CAULDRON).build());
		this.setDefaultState(this.stateFactory.getDefaultState().with(POWERED, false));
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos blockPos_2, boolean boolean_1) {
		boolean isPowered = world.isReceivingRedstonePower(pos);
		if (isPowered != state.get(POWERED)) {
			if (isPowered) {
				world.addBlockAction(pos, this, 0, 0);
			}

			world.setBlockState(pos, state.with(POWERED, isPowered), 3);
		}
	}

	@Override
	public boolean onBlockAction(BlockState blockState_1, World world_1, BlockPos blockPos_1, int int_1, int int_2) {
		world_1.playSound(null, blockPos_1, Wool.ALARM_SOUND_EVENT, SoundCategory.RECORDS, 3.0f, 1);
		return true;
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
		stateFactory$Builder_1.add(POWERED);
	}
}
