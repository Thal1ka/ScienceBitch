package com.sciencebitch.entity.items;

import net.minecraft.entity.item.EntityItem;

public class EntityItemNatriumChunk extends DropItemEntityBase {

	public EntityItemNatriumChunk(EntityItem entityItem) {
		super(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (isInWater()) {

			this.setDead();
			this.world.createExplosion(this, this.posX, this.posY + this.height / 16.0F + 1, this.posZ, 1.0F, true);
		}
	}
}
