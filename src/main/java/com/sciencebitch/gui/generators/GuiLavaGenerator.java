package com.sciencebitch.gui.generators;

import com.sciencebitch.containers.generators.ContainerLavaGenerator;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.generators.TileEntityLavaGenerator;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiLavaGenerator extends GuiContainer {

	private static final ResourceLocation TEXTURES = new ResourceLocation(ScienceBitch.MODID + ":textures/gui/container/lava_generator.png");
	private static final int DEFAULT_FONT_COLOR = 0xffffff;

	private final InventoryPlayer playerInventory;
	private final TileEntityLavaGenerator tileEntity;

	public GuiLavaGenerator(InventoryPlayer playerInventory, TileEntityLavaGenerator tileEntity) {

		super(new ContainerLavaGenerator(playerInventory, tileEntity));

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

		drawEnergyBar();

		if (TileEntityLavaGenerator.isWorking(tileEntity)) {
			drawBurnBar();
		}
	}

	private void drawEnergyBar() {

		int energyScale = getEnergyStoredScaled(54);
		int mirrorEnergy = 54 - energyScale;
		this.drawTexturedModalRect(this.guiLeft + 13, this.guiTop + 16 + mirrorEnergy, 176, mirrorEnergy + 31, 21, energyScale);
	}

	private void drawBurnBar() {

		int burnScale = getBurnScaled(13);
		int mirrorBurn = 13 - burnScale;

		this.drawTexturedModalRect(this.guiLeft + 56, this.guiTop + 37 + mirrorBurn, 176, mirrorBurn, 14, burnScale);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		String tileName = this.tileEntity.getDisplayName().getUnformattedComponentText();
		int stringWidth = fontRenderer.getStringWidth(tileName);
		this.fontRenderer.drawString(tileName, (this.xSize - stringWidth) / 2, 5, DEFAULT_FONT_COLOR);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 122, this.ySize - 94, DEFAULT_FONT_COLOR);
	}

	private int getEnergyStoredScaled(int pixels) {

		int totalEnergy = this.tileEntity.ENERGY_CAPACITY;
		int currentEnergy = this.tileEntity.getField(2);

		return (int) (currentEnergy * pixels / (double) totalEnergy + 0.5);
	}

	private int getBurnScaled(int pixels) {

		int currentBurnTime = this.tileEntity.getField(0);
		int totalBurnTime = this.tileEntity.getField(1);

		return (int) ((totalBurnTime - currentBurnTime) * pixels / (double) totalBurnTime + 0.5);
	}
}
