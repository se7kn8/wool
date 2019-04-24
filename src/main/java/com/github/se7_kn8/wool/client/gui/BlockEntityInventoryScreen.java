package com.github.se7_kn8.wool.client.gui;

import com.github.se7_kn8.wool.container.BlockEntityInventoryContainer;

public abstract class BlockEntityInventoryScreen<T extends BlockEntityInventoryContainer> extends BaseContainerScreen<T> {

	public BlockEntityInventoryScreen(T container) {
		super(container);
	}

	@Override
	protected void drawForeground(int int_1, int int_2) {
		String inventoryName = this.title.getFormattedText();
		this.font.draw(inventoryName, (float) (this.containerWidth / 2 - this.font.getStringWidth(inventoryName) / 2), 6.0F, 4210752);
		this.font.draw(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.containerHeight - 96 + 2), 4210752);
	}
}
