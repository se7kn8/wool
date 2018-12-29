package com.github.se7_kn8.wool.container;

import net.minecraft.container.Container;
import net.minecraft.inventory.Inventory;

public abstract class BlockEntityInventoryContainer extends Container {

	public abstract Inventory getPlayerInventory();

	public abstract Inventory getBlockInventory();

}
