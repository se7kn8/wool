package com.github.se7_kn8.wool.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.ContainerGui;
import net.minecraft.container.Container;
import net.minecraft.util.Identifier;

public abstract class BaseContainerGui extends ContainerGui {

	public BaseContainerGui(Container container_1) {
		super(container_1);
	}

	@Override
	protected void drawBackground(float var1, int var2, int var3) {
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.client.getTextureManager().bindTexture(getBackground());
		this.drawTexturedRect((this.width - this.containerWidth) / 2, (this.height - this.containerHeight) / 2, 0, 0, this.containerWidth, this.containerHeight);
	}

	@Override
	public void draw(int int_1, int int_2, float float_1) {
		this.drawBackground();
		super.draw(int_1, int_2, float_1);
		this.drawMousoverTooltip(int_1, int_2);
	}

	protected abstract Identifier getBackground();
}
