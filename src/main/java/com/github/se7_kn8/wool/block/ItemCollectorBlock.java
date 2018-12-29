package com.github.se7_kn8.wool.block;

import com.github.se7_kn8.wool.block.entity.BaseBlockEntity;
import net.fabricmc.fabric.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public class ItemCollectorBlock<T extends BaseBlockEntity> extends BaseBlockWithEntity<T> {

	public ItemCollectorBlock(Supplier<T> blockEntitySupplier) {
		super(blockEntitySupplier, FabricBlockSettings.of(Material.STONE).build());
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		world.addParticle(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 0, 0.5, -2);
		world.addParticle(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 0, 0.5, 2);
		world.addParticle(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, -2, 0.5, 0);
		world.addParticle(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 2, 0.5, 0);
	}
}
