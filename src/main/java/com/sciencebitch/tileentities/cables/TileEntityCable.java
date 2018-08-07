package com.sciencebitch.tileentities.cables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sciencebitch.interfaces.energy.IEnergyConnector;
import com.sciencebitch.util.EnergyHelper;
import com.sciencebitch.util.EnergyStoragePosition;

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
	private final int maxTransferRate = 10;

	private int connectorCurrent;

	private final Set<IEnergyConnector> connectedCables = new HashSet<>();
	private final Set<EnergyStoragePosition> connectedStorages = new HashSet<>();
	private final Set<IEnergyStorage> usedStorages = new HashSet<>();

	public TileEntityCable() {

	}

	@Override
	public void update() {

		sendEnergy();
		usedStorages.clear();

		if (connectorCurrent > maxTransferRate) {

			// TODO break cable
		}

		shockEntities();
	}

	private void sendEnergy() {

		for (EnergyStoragePosition storagePos : connectedStorages) {
			IEnergyStorage storage = storagePos.getStorage();
			if (storage.canExtract() && storage.getEnergyStored() > 0 && !usedStorages.contains(storage)) {
				EnergyHelper.transferEnergyThroughConnectors(storage, getStorageConnectors(storagePos));
			}
		}
	}

	private List<IEnergyConnector> getStorageConnectors(EnergyStoragePosition storagePos) {

		BlockPos pos = storagePos.getPosition();
		BlockPos[] neighbors = new BlockPos[] { pos.up(), pos.down(), pos.north(), pos.west(), pos.south(), pos.east() };

		List<IEnergyConnector> connectedConnectors = new ArrayList<>();

		for (BlockPos neighbor : neighbors) {

			TileEntity tileentity = world.getTileEntity(neighbor);

			if (tileentity != null && tileentity instanceof IEnergyConnector) {
				connectedConnectors.add((IEnergyConnector) tileentity);
			}
		}

		return connectedConnectors;
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

		for (EnergyStoragePosition storagePosition : connectedStorages) {
			if (storagePosition.getStorage().canReceive()) {
				consumers.add(storagePosition.getStorage());
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

	public void addConnection(EnergyStoragePosition storage) {
		connectedStorages.add(storage);
	}

	@Override
	public void addUsedStorage(IEnergyStorage storage) {
		usedStorages.add(storage);
	}

	public void clearConnections() {

		connectedCables.clear();
		connectedStorages.clear();
	}
}
