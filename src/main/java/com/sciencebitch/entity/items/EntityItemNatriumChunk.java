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
			this.world.createExplosion(this, this.posX, this.posY + 1, this.posZ, 10.0F, false);
			this.setDead();
		}
	}
}
