package com.github.se7_kn8.wool.client.gui;

import com.github.se7_kn8.wool.container.BlockEntityInventoryContainer;
import net.minecraft.inventory.Inventory;

public abstract class BlockEntityInventoryGui extends BaseContainerGui {

	private final Inventory playerInventory;
	private final Inventory blockInventory;

	public BlockEntityInventoryGui(BlockEntityInventoryContainer container_1) {
		super(container_1);
		this.playerInventory = container_1.getPlayerInventory();
		this.blockInventory = container_1.getBlockInventory();
	}

	@Override
	protected void drawForeground(int int_1, int int_2) {
		String inventoryName = this.blockInventory.getDisplayName().getFormattedText();
		this.fontRenderer.draw(inventoryName, (float) (this.containerWidth / 2 - this.fontRenderer.getStringWidth(inventoryName) / 2), 6.0F, 4210752);
		this.fontRenderer.draw(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.containerHeight - 96 + 2), 4210752);
	}

}
