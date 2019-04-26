package com.github.se7_kn8.wool.block.entity;

import com.github.se7_kn8.wool.Wool;
import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;

import java.util.function.Predicate;

public class WoodCollectorBlockEntity extends ItemCollectorBlockEntity {

	public WoodCollectorBlockEntity() {
		super(Wool.WOOD_COLLECTOR_BLOCK_ENTITY);
	}

	@Override
	protected Predicate<Item> getCollectPredicate() {
		return item -> (ItemTags.LOGS.contains(item) || ItemTags.LEAVES.contains(item) || ItemTags.SAPLINGS.contains(item));
	}

	@Override
	public String getContainerId() {
		return "wood_collector";
	}
}
