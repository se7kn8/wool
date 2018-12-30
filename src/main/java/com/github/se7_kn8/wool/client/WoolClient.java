package com.github.se7_kn8.wool.client;

import com.github.se7_kn8.wool.Wool;
import com.github.se7_kn8.wool.client.gui.BlockEntityInventoryGui;
import com.github.se7_kn8.wool.container.BlockEntityInventoryContainer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.gui.GuiFactory;
import net.fabricmc.fabric.api.client.gui.GuiProviderRegistry;
import net.minecraft.util.Identifier;

public class WoolClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		GuiProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "shearer"), createBasicInventoryGui("textures/gui/container/shearer.png"));
		GuiProviderRegistry.INSTANCE.registerFactory(new Identifier(Wool.MODID, "wool_collector"), createBasicInventoryGui(new Identifier("textures/gui/container/dispenser.png"))); // Use minecraft texture

	}

	private <T extends BlockEntityInventoryContainer> GuiFactory<T> createBasicInventoryGui(Identifier identifier) {
		return container -> new BlockEntityInventoryGui(container) {
			@Override
			protected Identifier getBackground() {
				return identifier;
			}
		};
	}

	private <T extends BlockEntityInventoryContainer> GuiFactory<T> createBasicInventoryGui(String backgroundPath) {
		return createBasicInventoryGui(new Identifier(Wool.MODID, backgroundPath));
	}

}