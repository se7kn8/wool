package com.github.se7_kn8.wool.block.entity;

import com.github.se7_kn8.wool.Wool;
import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

public class TreeCutterBlockEntity extends BaseBlockEntity implements Tickable {

	private static final int DAMAGE_PER_BLOCK = 2;

	private final int COOLDOWN_TICKS = 20;
	private int cooldownTicks = COOLDOWN_TICKS;

	public TreeCutterBlockEntity() {
		super(Wool.TREE_CUTTER_BLOCK_ENTITY);
	}

	@Override
	public void tick() {
		if (!this.world.isClient) {
			if (cooldownTicks <= 0) {

				block:
				for (int x = this.pos.getX() - 2; x < this.pos.getX() + 3; x++) {
					for (int z = this.pos.getZ() - 2; z < this.pos.getZ() + 3; z++) {
						for (int y = this.pos.getY(); y < this.pos.getY() + 10; y++) {
							BlockPos pos = new BlockPos(x, y, z);
							BlockState block = world.getBlockState(pos);
							if (BlockTags.LOGS.contains(block.getBlock())) {

								for (int i = 0; i < inventory.size(); i++) {
									ItemStack stack = inventory.get(i);
									if (!stack.isEmpty() && stack.getItem() instanceof AxeItem) {
										if (stack.getItem().getDurability() - stack.getDamage() >= DAMAGE_PER_BLOCK) {
											stack.setDamage(stack.getDamage() + DAMAGE_PER_BLOCK);
											world.breakBlock(pos, true);
											break block;
										} else {
											inventory.set(i, ItemStack.EMPTY);
										}
									}
								}
							}
						}
					}
				}

				cooldownTicks = COOLDOWN_TICKS;
			}
			cooldownTicks--;
		}
	}

	@Override
	public String getContainerId() {
		return "tree_cutter";
	}

	@Override
	public int getInvSize() {
		return 3;
	}

	@Override
	public void fromTag(CompoundTag compoundTag) {
		super.fromTag(compoundTag);
		cooldownTicks = compoundTag.getInt("cooldownTicks");
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		super.toTag(compoundTag);
		compoundTag.putInt("cooldownTicks", cooldownTicks);
		return compoundTag;
	}

}
