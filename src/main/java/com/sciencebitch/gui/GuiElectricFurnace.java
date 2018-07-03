package com.sciencebitch.gui;

import com.sciencebitch.containers.ContainerElectricFurnace;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.TileEntityElectricFurnace;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiElectricFurnace extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(ScienceBitch.MODID + ":textures/gui/container/electric_furnace.png");
	private final InventoryPlayer playerInventory;
	private final TileEntityElectricFurnace tileEntity;

	public GuiElectricFurnace(InventoryPlayer playerInventory, TileEntityElectricFurnace tileEntity) {

		super(new ContainerElectricFurnace(playerInventory, tileEntity));

		this.playerInventory = playerInventory;
		this.tileEntity = tileEntity;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		GlStateManager.color(1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		if (TileEntityElectricFurnace.isBurning(tileEntity)) {

			int burnScale = getBurnLeftScaled(13);
			int mirrorBurn = 12 - burnScale;
			this.drawTexturedModalRect(this.guiLeft + 57, this.guiTop + 37 + mirrorBurn, 176, mirrorBurn, 14, burnScale);
		}

		int cookScale = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(this.guiLeft + 79, this.guiTop + 35, 176, 14, cookScale + 1, 16);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		String tileName = this.tileEntity.getDisplayName().getUnformattedComponentText();
		int stringWidth = fontRenderer.getStringWidth(tileName);
		this.fontRenderer.drawString(tileName, (this.xSize - stringWidth) / 2, 8, 0x404040);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 122, this.ySize - 94, 0x404040);
	}

	private int getBurnLeftScaled(int pixels) {

		int currentBurnTime = this.tileEntity.getField(1);
		int burnTime = this.tileEntity.getField(0);

		if (currentBurnTime == 0) {
			currentBurnTime = 200;
		}

		return burnTime * pixels / currentBurnTime;
	}

	private int getCookProgressScaled(int pixels) {

		int cookTime = this.tileEntity.getField(2);
		int totalCookTime = this.tileEntity.getField(3);

		if (cookTime == 0 || totalCookTime == 0) return 0;
		return cookTime * pixels / totalCookTime;
	}
}
