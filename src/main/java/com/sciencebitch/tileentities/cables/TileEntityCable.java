package com.sciencebitch.tileentities.cables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sciencebitch.interfaces.energy.IEnergyConnector;
import com.sciencebitch.util.energy.EnergyHelper;
import com.sciencebitch.util.energy.EnergyStoragePosition;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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

	private BlockPos[] neighbors = new BlockPos[] { pos.north(), pos.west(), pos.south(), pos.east(), pos.up(), pos.down() };
	private final int maxTransferRate = 40;

	private int connectorCurrent;

	private final Set<IEnergyConnector> connectedCables = new HashSet<>();
	private final Set<EnergyStoragePosition> connectedStorages = new HashSet<>();
	private final Set<IEnergyStorage> masteredStorages = new HashSet<>();

	@Override
	public void setPos(BlockPos pos) {

		neighbors = new BlockPos[] { pos.north(), pos.west(), pos.south(), pos.east(), pos.up(), pos.down() };
		super.setPos(pos);
	}

	@Override
	public void update() {

		updateConnections();
		sendEnergy();

		if (connectorCurrent > maxTransferRate) {
			melt();
		}

		shockEntities();
		connectorCurrent = 0;
	}

	public void melt() {

		world.removeTileEntity(pos);

		IBlockState state = Blocks.FLOWING_LAVA.getStateFromMeta(1);
		world.setBlockState(pos, state, 3);
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

	private void sendEnergy() {

		for (EnergyStoragePosition storagePos : connectedStorages) {

			IEnergyStorage storage = storagePos.getStorage();

			if (storage.canExtract() && storage.getEnergyStored() > 0) {

				List<IEnergyConnector> storageConnectors = getStorageConnectors(storagePos);
				for (IEnergyConnector connector : storageConnectors) {
					if (!connector.equals(this) && connector.isMaster(storage)) return;
				}
				masteredStorages.add(storage);

				EnergyHelper.transferEnergyThroughConnectors(storage, storageConnectors);
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

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		connectorCurrent = nbt.getInteger("connectorCurrent");

		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		nbt.setInteger("connectorCurrent", connectorCurrent);

		return super.writeToNBT(nbt);
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

	private void updateConnections() {

		clearConnections();

		for (BlockPos neighbor : neighbors) {

			TileEntity tileentity = world.getTileEntity(neighbor);

			if (tileentity instanceof IEnergyConnector) {
				connectedCables.add((IEnergyConnector) tileentity);
			} else if (tileentity instanceof IEnergyStorage) {
				connectedStorages.add(new EnergyStoragePosition((IEnergyStorage) tileentity, neighbor));
			}
		}
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

	public void clearConnections() {

		connectedCables.clear();
		connectedStorages.clear();
	}

	@Override
	public boolean isMaster(IEnergyStorage storage) {
		return masteredStorages.contains(storage);
	}

	@Override
	public float getLoss() {
		return 0.1F;
	}
}
