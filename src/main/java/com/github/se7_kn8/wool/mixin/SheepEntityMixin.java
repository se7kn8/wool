package com.github.se7_kn8.wool.mixin;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(SheepEntity.class)
public class SheepEntityMixin {

	@Overwrite
	public static DyeColor method_6632(Random random_1) {
		return DyeColor.byId(random_1.nextInt(16));
	}

}
