package com.github.se7_kn8.wool.block.entity;

import com.github.se7_kn8.wool.Wool;
import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;

import java.util.function.Predicate;

public class WoolCollectorBlockEntity extends ItemCollectorBlockEntity {

	public WoolCollectorBlockEntity() {
		super(Wool.WOOL_COLLECTOR_BLOCK_ENTITY);
	}

	@Override
	protected Predicate<Item> getCollectPredicate() {
		return ItemTags.WOOL::contains;
	}

	@Override
	public String getContainerId() {
		return "wool_collector";
	}
}
