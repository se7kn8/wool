package com.github.se7_kn8.wool.util;

import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;

import java.util.function.Function;

public class ContainerUtil {

	public static void addPlayerSlots(Inventory playerInventory, Function<Slot, Slot> addSlotFunction) {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotFunction.apply(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlotFunction.apply(new Slot(playerInventory, x, 8 + x * 18, 142));
		}
	}

}
