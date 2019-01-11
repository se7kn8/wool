package com.github.se7_kn8.wool.container;

import com.github.se7_kn8.wool.Wool;
import com.github.se7_kn8.wool.util.ContainerUtil;
import net.minecraft.class_3917;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;

public class ShearerContainer extends BlockEntityInventoryContainer {

	private final Inventory blockInventory;

	public ShearerContainer(int syncId, Inventory shearer, PlayerInventory player) {
		super(syncId, player);
		this.blockInventory = shearer;

		for (int x = 0; x < 3; ++x) {
			this.addSlot(new Slot(shearer, x, 62 + x * 18, 35) {
				@Override
				public boolean canInsert(ItemStack itemStack_1) {
					return itemStack_1.getItem() == Items.SHEARS;
				}
			});
		}

		ContainerUtil.addPlayerSlots(player, this::addSlot);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.blockInventory.canPlayerUseInv(player);
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

	@Override
	public class_3917<?> method_17358() {
		return null;
	}

	@Override
	public TextComponent getTextComponent() {
		return new TranslatableTextComponent("container." + Wool.MODID + ".shearer");
	}
}
