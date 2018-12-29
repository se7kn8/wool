package com.github.se7_kn8.wool.container;

import com.github.se7_kn8.wool.util.ContainerUtil;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ShearerContainer extends BlockEntityInventoryContainer {

	private final Inventory blockInventory;
	private final Inventory playerInventory;

	public ShearerContainer(Inventory shearer, Inventory player) {
		this.blockInventory = shearer;
		this.playerInventory = player;

		for (int x = 0; x < 3; ++x) {
			this.addSlot(new Slot(shearer, x, 62 + x * 18, 35) {
				@Override
				public boolean canInsert(ItemStack itemStack_1) {
					return itemStack_1.getItem() == Items.SHEARS;
				}
			});
		}

		ContainerUtil.addPlayerSlots(playerInventory, this::addSlot);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.blockInventory.canPlayerUseInv(player);
	}


	public Inventory getBlockInventory() {
		return blockInventory;
	}

	public Inventory getPlayerInventory() {
		return playerInventory;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slotList.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();
			if (index < 3) {
				if (!this.insertItem(slotStack, 3, 39, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(slotStack, 0, 3, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (slotStack.getAmount() == stack.getAmount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, slotStack);
		}

		return stack;
	}
}
