package anaso.WheatHarvest;

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;

public class GuiReverseVisible extends GuiIngame
{
	public GuiReverseVisible(Minecraft par1Minecraft)
	{
		super(par1Minecraft);
	}

	private int X = 0, Y = 0;

	@Override
	public void renderGameOverlay(float par1, boolean par2, int par3, int par4)
	{
		Minecraft MC = ModLoader.getMinecraftInstance();

		if(MC.objectMouseOver.hitVec.yCoord - MC.objectMouseOver.blockY > 0.5D && MC.objectMouseOver.hitVec.yCoord - MC.objectMouseOver.blockY != 1)
		{
			ScaledResolution XY = new ScaledResolution(MC.gameSettings, MC.displayWidth, MC.displayHeight);
			X = (XY.getScaledHeight() / 2) - (XY.getScaledHeight() / 20);
			Y = (XY.getScaledWidth() / 2);
			//System.out.println("X:" + X + "  Y:" + Y);
			MC.fontRenderer.drawStringWithShadow("Reverse", Y, X, 16777215);
		}
		else if((MC.objectMouseOver.hitVec.yCoord - MC.objectMouseOver.blockY < 0.5D && MC.objectMouseOver.hitVec.yCoord - MC.objectMouseOver.blockY != -1))
		{
			ScaledResolution XY = new ScaledResolution(MC.gameSettings, MC.displayWidth, MC.displayHeight);
			X = (XY.getScaledHeight() / 2) + (XY.getScaledHeight() / 20);
			Y = (XY.getScaledWidth() / 2);
			//System.out.println("X:" + X + "  Y:" + Y);
			MC.fontRenderer.drawStringWithShadow("Normal", Y, X, 16777215);
		}
	}
}