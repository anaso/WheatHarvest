package anaso.WheatHarvest;

import java.util.HashMap;
import java.util.logging.Level;
import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.src.*;
import net.minecraft.block.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.*;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

@Mod
(
	modid = "WheatHarvest",
	name = "Wheat Harvest",
	version = "1.5.2"
)

@NetworkMod
(
	clientSideRequired=true,
	serverSideRequired=false,
	channels={"anaso_WH"},
	packetHandler = PacketHandler.class
)

public class WheatHarvest
{
	public static boolean Wheat = true;
	public static boolean NetherWart = true;
	public static boolean PotatoAndCarrot = true;

	public static int MouseRightButton = -99;

	HashMap <String, Boolean> Options = new HashMap<String, Boolean>();

	@SidedProxy(clientSide = "anaso_WheatHarvest.ClientProxyTick", serverSide = "anaso_WheatHarvest.CommonProxyTick")
	public static CommonProxyTick proxyTick;

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			Property PropWheat  = cfg.get(cfg.CATEGORY_GENERAL, "Wheat", true);
			Property PropNetherWart = cfg.get(cfg.CATEGORY_GENERAL, "NetherWart", true);
			Property PropPotatoAndCarrot = cfg.get(cfg.CATEGORY_GENERAL, "PotatoAndCarrot", true);
			Wheat = PropWheat.getBoolean(true);
			NetherWart = PropNetherWart.getBoolean(true);
			PotatoAndCarrot = PropPotatoAndCarrot.getBoolean(true);

			Options.put("Wheat", Boolean.valueOf(Wheat));
			Options.put("NetherWart", Boolean.valueOf(NetherWart));
			Options.put("PotatoAndCarrot", Boolean.valueOf(PotatoAndCarrot));
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Error Message");
		}
		finally
		{
			cfg.save();
		}
	}

	@Mod.PostInit
	public void PostInit(FMLPostInitializationEvent event)
	{
		proxyTick.RegisterTicking(Options);
	}

	/*
	@Mod.Init
	public void Init()
	{
	}
	*/
}