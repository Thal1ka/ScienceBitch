package com.sciencebitch.entity.items;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;

public class EntityItemNatriumChunk extends DropItemEntityBase {

	private static final int BURN_TIME = 40;

	private final Random rnd;

	private boolean isIgnited;
	private int timeSinceIgnition;

	public EntityItemNatriumChunk(EntityItem entityItem) {
		super(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ);

		rnd = world.rand;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (isInWater() && !isIgnited) {
			ignite();
		}
	}

	@Override
	public void onEntityUpdate() {

		if (isIgnited && !world.isRemote) {

			timeSinceIgnition++;

			if (timeSinceIgnition >= BURN_TIME) {

				this.setDead();
				this.explode();

			} else {

				createBurningMotion();
			}
		}
		super.onEntityUpdate();
	}

	private void ignite() {

		this.isIgnited = true;
		this.setInfinitePickupDelay();
	}

	private void explode() {
		this.world.createExplosion(this, this.posX, this.posY + this.height / 16.0F + 1, this.posZ, 1.0F, true);
	}

	private void createBurningMotion() {

	}

}
