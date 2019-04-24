package com.github.se7_kn8.wool.client.gui;

import com.github.se7_kn8.wool.container.BlockEntityInventoryContainer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.util.Identifier;

public abstract class BaseContainerScreen<T extends BlockEntityInventoryContainer> extends ContainerScreen<T> {

	public BaseContainerScreen(T container) {
		super(container, container.getPlayerInventory(), container.getTextComponent());
	}

	@Override
	protected void drawBackground(float var1, int var2, int var3) {
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager().bindTexture(getBackground());
		this.blit((this.width - this.containerWidth) / 2, (this.height - this.containerHeight) / 2, 0, 0, this.containerWidth, this.containerHeight);
	}

	@Override
	public void render(int int_1, int int_2, float float_1) {
		this.renderBackground();
		super.render(int_1, int_2, float_1);
		this.drawMouseoverTooltip(int_1, int_2);
	}

	protected abstract Identifier getBackground();
}
