package anaso.WheatHarvest;

import java.util.HashMap;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.src.*;

@SideOnly(Side.CLIENT)
public class ClientProxyTick extends CommonProxyTick
{
	@Override
	public void RegisterTicking(HashMap Options)
	{
		TickRegistry.registerTickHandler(new WheatHarvestTick(Options), Side.CLIENT);
	}
}