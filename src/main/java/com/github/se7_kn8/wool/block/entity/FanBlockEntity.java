package com.github.se7_kn8.wool.block.entity;

import com.github.se7_kn8.wool.Wool;
import com.github.se7_kn8.wool.block.FanBlock;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class FanBlockEntity extends BaseBlockEntity implements Tickable {

	public static final int RANGE = 20;
	public static final double PARTICLE_SPEED = 0.3d;

	public FanBlockEntity() {
		super(Wool.FAN_BLOCK_ENTITY);
	}

	private int tickCounter;

	@Override
	public void tick() {
		boolean shouldUpdate = false;
		if (world.isClient) {
			tickCounter++;
			// TODO use on neighbor update
			BlockState state = world.getBlockState(this.pos);
			if (state.getBlock() == Wool.FAN_BLOCK && tickCounter >= 3) {
				boolean active = this.world.getBlockState(this.pos).get(FanBlock.ACTIVE);
				if (active) {
					tickCounter = 0;
					double velX = 0;
					double velY = 0;
					double velZ = 0;
					switch (state.get(FanBlock.DIRECTION)) {
						case NORTH:
							velZ = -1;
							break;
						case EAST:
							velX = 1;
							break;
						case WEST:
							velX = -1;
							break;
						case SOUTH:
							velZ = 1;
							break;
						case UP:
							velY = 1;
							break;
						case DOWN:
							velY = -1;
							break;
					}
					world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5d, pos.getY() + 0.5, pos.getZ() + 0.5d, velX * PARTICLE_SPEED, velY * PARTICLE_SPEED, velZ * PARTICLE_SPEED);
				}
			}
		} else {
			BlockState state = world.getBlockState(this.pos);
			if (state.getBlock() == Wool.FAN_BLOCK) {
				Direction dir = state.get(FanBlock.DIRECTION);
				int xOffset = 0;
				int yOffset = 0;
				int zOffset = 0;
				switch (dir) {
					case NORTH:
						zOffset = 1;
						break;
					case EAST:
						xOffset = -1;
						break;
					case WEST:
						xOffset = 1;
						break;
					case SOUTH:
						zOffset = -1;
						break;
					case UP:
						yOffset = 1;
						break;
					case DOWN:
						yOffset = -1;
						break;
				}

				BlockState blockStateBehind = world.getBlockState(new BlockPos(this.pos.getX() + xOffset, this.pos.getY() + yOffset, this.pos.getZ() + zOffset));
				BlockState blockStateBefore = world.getBlockState(new BlockPos(this.pos.getX() - xOffset, this.pos.getY() - yOffset, this.pos.getZ() - zOffset));
				shouldUpdate = true;
				boolean active;
				if ((blockStateBehind.isAir() || !blockStateBehind.isFullBoundsCubeForCulling()) && (blockStateBefore.isAir() || !blockStateBefore.isFullBoundsCubeForCulling())) {
					this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(FanBlock.ACTIVE, true));
					active = true;
				} else {
					this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(FanBlock.ACTIVE, false));
					active = false;
				}

				if (active) {
					int minXOffset = 0;
					int minYOffset = 0;
					int minZOffset = 0;
					int maxXOffset = 0;
					int maxYOffset = 0;
					int maxZOffset = 0;
					switch (dir) {
						case NORTH:
							minXOffset = 0;
							minYOffset = 0;
							minZOffset = 0;
							maxXOffset = 1;
							maxYOffset = 1;
							maxZOffset = -RANGE;
							break;
						case SOUTH:
							minXOffset = 0;
							minYOffset = 0;
							minZOffset = 1;
							maxXOffset = 1;
							maxYOffset = 1;
							maxZOffset = RANGE;
							break;
						case WEST:
							minXOffset = 0;
							minYOffset = 0;
							minZOffset = 0;
							maxXOffset = -RANGE;
							maxYOffset = 1;
							maxZOffset = 1;
							break;
						case EAST:
							minXOffset = 1;
							minYOffset = 0;
							minZOffset = 0;
							maxXOffset = RANGE;
							maxYOffset = 1;
							maxZOffset = 1;
							break;
						case UP:
							minXOffset = 0;
							minYOffset = 0;
							minZOffset = 0;
							maxXOffset = 1;
							maxYOffset = RANGE;
							maxZOffset = 1;
							break;
						case DOWN:
							minXOffset = 0;
							minYOffset = -1;
							minZOffset = 0;
							maxXOffset = 1;
							maxYOffset = -RANGE;
							maxZOffset = 1;
							break;
					}

					List<LivingEntity> entities = world.getEntities(LivingEntity.class, new BoundingBox(pos.getX() + minXOffset, pos.getY() + minYOffset, pos.getZ() + minZOffset, pos.getX() + maxXOffset, pos.getY() + maxYOffset, pos.getZ() + maxZOffset), entity -> true);
					for (LivingEntity entity : entities) {
						Vec3d velocityVec = new Vec3d(Math.abs(maxXOffset) == RANGE ? maxXOffset : 0, Math.abs(maxYOffset) == RANGE ? maxYOffset : 0, Math.abs(maxZOffset) == RANGE ? maxZOffset : 0);
						Vec3d blockVector = new Vec3d(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);
						Vec3d entityVector = new Vec3d(entity.x, entity.y, entity.z);
						//velocityVec = velocityVec.normalize().multiply((-0.005 * Math.pow(blockVector.distanceTo(entityVector), 2) + 0.5) * 0.2);
						velocityVec = velocityVec.normalize().multiply(0.1 * Math.pow(0.8, blockVector.distanceTo(entityVector)));
						if (entity instanceof ServerPlayerEntity) {
							PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
							buf.writeDouble(velocityVec.x);
							buf.writeDouble(velocityVec.y);
							buf.writeDouble(velocityVec.z);
							((ServerPlayerEntity) entity).networkHandler.sendPacket(new CustomPayloadS2CPacket(Wool.ADD_VELOCITY_TO_PLAYER_PACKET, buf));
						} else {
							entity.addVelocity(velocityVec.x, velocityVec.y, velocityVec.z);
						}
					}
				}
			}

		}
		if (shouldUpdate) {
			this.markDirty();
		}
	}

	@Override
	public int getInvSize() {
		return 1;
	}

	@Override
	public String getContainerId() {
		return "fan";
	}

}
