package com.github.se7_kn8.wool.client.gui;

import com.github.se7_kn8.wool.Wool;
import com.github.se7_kn8.wool.container.ShearerContainer;
import net.minecraft.util.Identifier;

public class ShearerGui extends BaseContainerGui<ShearerContainer> {
	private static final Identifier BACKGROUND = new Identifier(Wool.MODID, "");

	public ShearerGui(ShearerContainer container) {
		super(container);
	}

	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}
}
