package com.github.se7_kn8.wool.container;

import com.github.se7_kn8.wool.util.ContainerUtil;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ItemCollectorContainer extends BlockEntityInventoryContainer {

	private final Inventory blockInventory;
	private final Inventory playerInventory;

	public ItemCollectorContainer(Inventory collector, Inventory player) {
		this.blockInventory = collector;
		this.playerInventory = player;

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				this.addSlot(new Slot(blockInventory, x + y * 3, 62 + x * 18, 17 + y * 18) {
					@Override
					public boolean canInsert(ItemStack itemStack_1) {
						return blockInventory.isValidInvStack(this.id, itemStack_1);
					}
				});
			}
		}

		ContainerUtil.addPlayerSlots(playerInventory, this::addSlot);
	}

	@Override
	public boolean canUse(PlayerEntity var1) {
		return true;
	}

	@Override
	public Inventory getPlayerInventory() {
		return playerInventory;
	}

	@Override
	public Inventory getBlockInventory() {
		return blockInventory;
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot_1 = this.slotList.get(index);
		if (slot_1 != null && slot_1.hasStack()) {
			ItemStack slotStack = slot_1.getStack();
			stack = slotStack.copy();
			if (index < 9) {
				if (!this.insertItem(slotStack, 9, 45, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(slotStack, 0, 9, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot_1.setStack(ItemStack.EMPTY);
			} else {
				slot_1.markDirty();
			}

			if (slotStack.getAmount() == stack.getAmount()) {
				return ItemStack.EMPTY;
			}

			slot_1.onTakeItem(player, slotStack);
		}

		return stack;
	}

}
