package com.github.se7_kn8.wool.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.Predicate;

public abstract class ItemCollectorBlockEntity extends BaseBlockEntity implements Tickable, Inventory {

	public ItemCollectorBlockEntity(BlockEntityType<?> blockEntityType_1) {
		super(blockEntityType_1);
	}

	private int workCounter = 0;
	private static final int MAX_WORK = 5;

	@Override
	public void tick() {
		if (!world.isClient) {
			workCounter++;

			BoundingBox outerBox = new BoundingBox(pos.getX() - 4, pos.getY() - 1, pos.getZ() - 4, pos.getX() + 5, pos.getY() + 4, pos.getZ() + 5);
			BoundingBox innerBox = new BoundingBox(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);

			List<ItemEntity> items = world.method_8390(ItemEntity.class, outerBox, this::checkItemEntity);
			for (ItemEntity item : items) {
				if (!item.getBoundingBox().intersects(innerBox)) {
					Vec3d velocity = new Vec3d(item.x - (pos.getX() + 0.5), item.y - (pos.getY() + 1), item.z - (pos.getZ() + 0.5)).normalize().multiply(-0.1);
					item.addVelocity(velocity.x, velocity.y, velocity.z);
				}
			}
			if (workCounter >= MAX_WORK) {

				List<ItemEntity> nearItems = world.method_8390(ItemEntity.class, innerBox, this::checkItemEntity);
				if (nearItems.size() > 0) {
					ItemEntity entity = nearItems.get(0);
					for (int i = 0; i < inventory.size(); i++) {
						ItemStack stack = inventory.get(i);
						if (!stack.isEmpty() && stack.getAmount() < stack.getMaxAmount() && stack.getItem() == entity.getStack().getItem()) {
							entity.getStack().subtractAmount(1);
							stack.addAmount(1);
							break;
						} else if (stack.isEmpty()) {
							inventory.set(i, new ItemStack(entity.getStack().getItem(), 1));
							entity.getStack().subtractAmount(1);
							break;
						}
					}
				}
				workCounter = 0;
			}
		}
	}

	@Override
	public int getInvSize() {
		return 9;
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag_1) {
		super.toTag(compoundTag_1);
		compoundTag_1.putInt("workCounter", workCounter);
		return compoundTag_1;
	}

	@Override
	public void fromTag(CompoundTag compoundTag_1) {
		super.fromTag(compoundTag_1);
		workCounter = compoundTag_1.getInt("workCounter");
	}

	protected abstract Predicate<Item> getCollectPredicate();

	@Override
	public boolean isValidInvStack(int int_1, ItemStack itemStack_1) {
		return getCollectPredicate().test(itemStack_1.getItem());
	}

	private boolean checkItemEntity(ItemEntity toTest) {
		return getCollectPredicate().test(toTest.getStack().getItem());
	}

}
