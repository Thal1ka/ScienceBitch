package com.sciencebitch.tileentities.cables;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyConnector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityCable extends TileEntity implements ITickable, IEnergyConnector {

	public static final DamageSource electrocution = new DamageSource("electric").setDamageBypassesArmor();

	private final BlockPos[] neighbors = new BlockPos[] { pos.north(), pos.west(), pos.south(), pos.east(), pos.up(), pos.down() };
	private final int maxTransferRate;

	private int connectorCurrent;

	private final List<IEnergyConnector> connectedCables = new ArrayList<>();
	private final List<IEnergyStorage> connectedStorages = new ArrayList<>();

	public TileEntityCable(int maxTransferRate) {

		this.maxTransferRate = maxTransferRate;
	}

	@Override
	public void update() {

		if (connectorCurrent > maxTransferRate) {

			// TODO break cable
		}

		shockEntities();
	}

	public void shockEntities() {

		float rangeMultiplier = Math.min(0.25f + (float) Math.sqrt(connectorCurrent) / 512, 0.75f);

		BlockPos rangeOffset = new BlockPos(rangeMultiplier, rangeMultiplier, rangeMultiplier);
		BlockPos maxOffset = new BlockPos(1, 1, 1);
		AxisAlignedBB range = new AxisAlignedBB(this.pos.subtract(rangeOffset), this.pos.add(maxOffset).add(rangeOffset));
		List entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, range);

		for (int i = 0; i < entities.size(); i++) {
			shock((EntityLivingBase) entities.get(i), (float) Math.sqrt(connectorCurrent / 4));
		}
	}

	public void shock(EntityLivingBase target, float damage) {

		target.attackEntityFrom(electrocution, damage);
		target.setFire(10);
		this.getWorld().playSound(null, target.getPosition(), SoundEvents.BLOCK_NOTE_SNARE, SoundCategory.BLOCKS, 1.0f, 1.0f);
		target.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 2, 6, true, false));
		target.addPotionEffect(new PotionEffect(Potion.getPotionById(4), 2, 1, true, false));
		target.addPotionEffect(new PotionEffect(Potion.getPotionById(24), 2, 1, true, false));
		target.addPotionEffect(new PotionEffect(Potion.getPotionById(8), 2, -10, true, false));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return super.writeToNBT(compound);
	}

	@Override
	public List<IEnergyConnector> getConnectedCables() {

		return new ArrayList<>(connectedCables);
	}

	@Override
	public List<IEnergyStorage> getConnectedConsumers() {

		List<IEnergyStorage> consumers = new ArrayList<>();

		for (IEnergyStorage storage : connectedStorages) {
			if (storage.canReceive()) {
				consumers.add(storage);
			}
		}

		return consumers;
	}

	@Override
	public void addCurrentThroughConnector(int amount) {

		connectorCurrent += amount;
	}

	public void addConnection(IEnergyConnector connector) {
		connectedCables.add(connector);
	}

	public void addConnection(IEnergyStorage storage) {
		connectedStorages.add(storage);
	}
}
