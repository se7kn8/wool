package com.github.se7_kn8.wool.container;

import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TextComponent;

public abstract class BlockEntityInventoryContainer extends Container {

	private final PlayerInventory playerInventory;

	public BlockEntityInventoryContainer(int syncId, PlayerInventory inventory) {
		super(null, syncId);
		this.playerInventory = inventory;
	}

	public abstract TextComponent getTextComponent();

	public PlayerInventory getPlayerInventory() {
		return playerInventory;
	}

}
