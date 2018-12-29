package com.github.se7_kn8.wool.block.entity;

import com.github.se7_kn8.wool.Wool;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BoundingBox;

import java.util.List;

public class ShearerBlockEntity extends BaseBlockEntity implements Tickable, Inventory {

	private static final int MAX_DAMAGE_PER_TICK = 30;
	private static final int DAMAGE_PER_SHEEP = 5;

	public ShearerBlockEntity() {
		super(Wool.SHEEP_SHAVER_BLOCK_ENTITY);
	}

	private final int COOLDOWN_TICKS = 20 * 10;
	private int cooldownTicks = COOLDOWN_TICKS;

	@Override
	public void tick() {
		if (!this.world.isClient) {
			if (cooldownTicks <= 0) {
				List<SheepEntity> sheeps = world.getEntities(SheepEntity.class, new BoundingBox(this.pos.getX() - 1, this.pos.getY() - 1, this.pos.getZ() - 1, this.pos.getX() + 2, this.pos.getY() + 4, this.pos.getZ() + 2), sheep -> !(sheep.isSheared() || sheep.isChild()));

				int totalDamage = 0;

				for (SheepEntity sheep : sheeps) {
					totalDamage += DAMAGE_PER_SHEEP;
					int currentDamage = DAMAGE_PER_SHEEP;

					for (int i = 0; i < inventory.size(); i++) {
						ItemStack stack = inventory.get(i);
						if (!stack.isEmpty() && stack.getItem() instanceof ShearsItem) {
							if (stack.getItem().getDurability() - stack.getDamage() >= currentDamage) {
								stack.setDamage(stack.getDamage() + currentDamage);
								sheep.method_6636();
								break;
							} else {
								int damageLeft = stack.getItem().getDurability() - stack.getDamage();
								currentDamage -= damageLeft;
								inventory.set(i, ItemStack.EMPTY);
							}
						}
					}

					if (totalDamage >= MAX_DAMAGE_PER_TICK) {
						break;
					}
				}

				cooldownTicks = COOLDOWN_TICKS;
			}
			cooldownTicks--;
		}
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

	@Override
	public int getInvSize() {
		return 3;
	}

	@Override
	public boolean isValidInvStack(int int_1, ItemStack itemStack_1) {
		return itemStack_1.getItem() instanceof ShearsItem;
	}

	@Override
	public String getContainerId() {
		return "shearer";
	}
}
