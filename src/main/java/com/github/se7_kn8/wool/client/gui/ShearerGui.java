package com.github.se7_kn8.wool.client.gui;

import com.github.se7_kn8.wool.Wool;
import com.github.se7_kn8.wool.container.ShearerContainer;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.Identifier;

public class ShearerGui extends BaseContainerGui {
	private static final Identifier BACKGROUND = new Identifier(Wool.MODID, "");
	private final Inventory playerInventory;
	private final Inventory blockInventory;

	public ShearerGui(ShearerContainer container) {
		super(container);
		this.playerInventory = container.getPlayerInventory();
		this.blockInventory = container.getBlockInventory();
	}

	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}
}
