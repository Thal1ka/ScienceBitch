package com.sciencebitch.entity.items;

import net.minecraft.entity.item.EntityItem;

public class DropItemEntityBase extends EntityItem {

	public DropItemEntityBase(EntityItem entityItem, double x, double y, double z) {

		super(entityItem.getEntityWorld(), x, y, z, entityItem.getItem());

		setPickupDelay(40);
		setThrower(entityItem.getThrower());

		this.motionX = entityItem.motionX;
		this.motionY = entityItem.motionY;
		this.motionZ = entityItem.motionZ;
	}

}
