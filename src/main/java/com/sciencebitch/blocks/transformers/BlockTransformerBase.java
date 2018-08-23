package com.sciencebitch.blocks.transformers;

import com.sciencebitch.blocks.machines.BlockMachineBase;
import com.sciencebitch.tileentities.transformers.TileEntityTransformer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockTransformerBase extends BlockMachineBase {

	private Voltage input;
	private Voltage output;

	public BlockTransformerBase(String name, Voltage input, Voltage output) {

		super(name, false, -1);

		this.input = input;
		this.output = output;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTransformer();
	}

	public boolean canConnect(IBlockState transformerState, Block cable, EnumFacing cableDirection) {

		Voltage singleSide, multiSide;

		if (input.isGreater(output)) {
			singleSide = input;
			multiSide = output;
		} else {
			singleSide = output;
			multiSide = input;
		}

		if (cableDirection == transformerState.getValue(BlockTransformerBase.FACING)) return singleSide.canConnectTo(cable);

		return multiSide.canConnectTo(cable);
	}
}
