package com.sciencebitch.gui;

import com.sciencebitch.containers.ContainerPulverizer;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.machines.TileEntityPulverizer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPulverizer extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(ScienceBitch.MODID + ":textures/gui/container/pulverizer.png");
	private static final int DEFAULT_FONT_COLOR = 0xffffff;

	private final InventoryPlayer playerInventory;
	private final TileEntityPulverizer tileEntity;

	public GuiPulverizer(InventoryPlayer playerInventory, TileEntityPulverizer tileEntity) {

		super(new ContainerPulverizer(playerInventory, tileEntity));

		this.playerInventory = playerInventory;
		this.tileEntity = tileEntity;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		GlStateManager.color(1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		if (TileEntityPulverizer.isWorking(tileEntity)) {

			int energyLeft = getEnergyLeftScaled(54);
			int mirrorEnergy = 54 - energyLeft;
			this.drawTexturedModalRect(this.guiLeft + 13, this.guiTop + 16 + mirrorEnergy, 176, mirrorEnergy + 31, 21, energyLeft);
		}

		int cookScale = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(this.guiLeft + 79, this.guiTop + 35, 176, 14, cookScale + 1, 16);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		String tileName = this.tileEntity.getDisplayName().getUnformattedComponentText();
		int stringWidth = fontRenderer.getStringWidth(tileName);
		this.fontRenderer.drawString(tileName, (this.xSize - stringWidth) / 2, 5, DEFAULT_FONT_COLOR);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 122, this.ySize - 94, DEFAULT_FONT_COLOR);
	}

	private int getEnergyLeftScaled(int pixels) {

		int totalEnergy = this.tileEntity.ENERGY_CAPACITY;
		int currentEnergy = this.tileEntity.getField(0);

		return (int) (currentEnergy * pixels / (double) totalEnergy + 0.5);
	}

	private int getCookProgressScaled(int pixels) {

		int cookTime = this.tileEntity.getField(1);
		int totalCookTime = this.tileEntity.getField(2);

		if (cookTime == 0 || totalCookTime == 0)
			return 0;
		return (int) (cookTime * pixels / (double) totalCookTime + 0.5);
	}
}
