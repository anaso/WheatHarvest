package anaso.WheatHarvest;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.EnumSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.ModLoader;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class WheatHarvestTick implements ITickHandler
{
	HashMap <String, Boolean> Options = new HashMap<String, Boolean>();

	private final EnumSet<TickType> tickSet = EnumSet.of(TickType.RENDER);

	int WheatID = -1;
	int NetherWartID = -1;
	int PotatoID = -1;
	int CarrotID = -1;

	int Suspend = 0;

	public WheatHarvestTick(HashMap Options)
	{
		this.Options = Options;

		if(this.Options.get("Wheat").booleanValue())
		{
			WheatID = Item.seeds.itemID;
		}

		if(this.Options.get("NetherWart").booleanValue())
		{
			NetherWartID = Item.netherStalkSeeds.itemID;
		}

		if(this.Options.get("PotatoAndCarrot").booleanValue())
		{
			PotatoID = Item.potato.itemID;
			CarrotID = Item.carrot.itemID;
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		Minecraft MC = ModLoader.getMinecraftInstance();

		if(MC.theWorld != null && Suspend == 0)
		{
			if(MC.thePlayer.inventory.mainInventory[MC.thePlayer.inventory.currentItem] != null && MC.objectMouseOver != null)
				{
				int HaveItem = MC.thePlayer.inventory.mainInventory[MC.thePlayer.inventory.currentItem].itemID;
				//System.out.println("HaveItem: " + HaveItem + "  KeyState:" + MC.gameSettings.keyBindUseItem.pressed);

				int WorldX = MC.objectMouseOver.blockX;
				int WorldY = MC.objectMouseOver.blockY;
				int WorldZ = MC.objectMouseOver.blockZ;

				if(HaveItem == WheatID && MC.gameSettings.keyBindUseItem.pressed)
				{
					WorldServer WS = ModLoader.getMinecraftServerInstance().worldServers[MC.theWorld.getWorldInfo().getVanillaDimension()];

					//if(MC.theWorld.getBlockId(WorldX, WorldY, WorldZ) == Block.crops.blockID && MC.theWorld.getBlockMetadata(WorldX, WorldY, WorldZ) == 7)
					if(WS.getBlockId(WorldX, WorldY, WorldZ) == Block.crops.blockID && WS.getBlockMetadata(WorldX, WorldY, WorldZ) == 7)
					{
						if(SendPackets(WorldX, WorldY, WorldZ, MC.theWorld.getWorldInfo().getVanillaDimension(), Block.crops.blockID, HaveItem, Item.wheat.itemID))
						{
							Suspend = 4;
							MC.theWorld.setBlockMetadataWithNotify(WorldX, WorldY, WorldZ, 0, 0);
						}
					}
				}

				else if(HaveItem == NetherWartID && MC.gameSettings.keyBindUseItem.pressed)
				{
					WorldServer WS = ModLoader.getMinecraftServerInstance().worldServers[MC.theWorld.getWorldInfo().getVanillaDimension()];

					//if(MC.theWorld.getBlockId(WorldX, WorldY, WorldZ) == Block.crops.blockID && MC.theWorld.getBlockMetadata(WorldX, WorldY, WorldZ) == 7)
					if(WS.getBlockId(WorldX, WorldY, WorldZ) == Block.netherStalk.blockID && WS.getBlockMetadata(WorldX, WorldY, WorldZ) == 3)
					{
						if(SendPackets(WorldX, WorldY, WorldZ, MC.theWorld.getWorldInfo().getVanillaDimension(), Block.netherStalk.blockID, HaveItem, Item.netherStalkSeeds.itemID))
						{
							Suspend = 4;
							MC.theWorld.setBlockMetadataWithNotify(WorldX, WorldY, WorldZ, 0, 0);
						}
					}
				}

				else if(HaveItem == PotatoID && MC.gameSettings.keyBindUseItem.pressed)
				{
					WorldServer WS = ModLoader.getMinecraftServerInstance().worldServers[MC.theWorld.getWorldInfo().getVanillaDimension()];

					//if(MC.theWorld.getBlockId(WorldX, WorldY, WorldZ) == Block.crops.blockID && MC.theWorld.getBlockMetadata(WorldX, WorldY, WorldZ) == 7)
					if(WS.getBlockId(WorldX, WorldY, WorldZ) == Block.potato.blockID && WS.getBlockMetadata(WorldX, WorldY, WorldZ) == 7)
					{
						if(SendPackets(WorldX, WorldY, WorldZ, MC.theWorld.getWorldInfo().getVanillaDimension(), Block.potato.blockID, HaveItem, Item.potato.itemID))
						{
							Suspend = 4;
							MC.theWorld.setBlockMetadataWithNotify(WorldX, WorldY, WorldZ, 0, 0);
						}
					}
				}

				else if(HaveItem == CarrotID && MC.gameSettings.keyBindUseItem.pressed)
				{
					WorldServer WS = ModLoader.getMinecraftServerInstance().worldServers[MC.theWorld.getWorldInfo().getVanillaDimension()];

					//if(MC.theWorld.getBlockId(WorldX, WorldY, WorldZ) == Block.crops.blockID && MC.theWorld.getBlockMetadata(WorldX, WorldY, WorldZ) == 7)
					if(WS.getBlockId(WorldX, WorldY, WorldZ) == Block.carrot.blockID && WS.getBlockMetadata(WorldX, WorldY, WorldZ) == 7)
					{
						if(SendPackets(WorldX, WorldY, WorldZ, MC.theWorld.getWorldInfo().getVanillaDimension(), Block.carrot.blockID, HaveItem, Item.carrot.itemID))
						{
							Suspend = 4;
							MC.theWorld.setBlockMetadataWithNotify(WorldX, WorldY, WorldZ, 0, 0);
						}
					}
				}

			}
		}
		else if(Suspend > 0)
		{
			Suspend--;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return tickSet;
	}

	@Override
	public String getLabel() { return null; }

	public boolean SendPackets(int WorldX, int WorldY, int WorldZ, int DimensionNo, int BlockID, int SeedID, int WheatID)
	{
		boolean Check = false;
		Random Rand = new Random(System.currentTimeMillis());
		int RandInt = Rand.nextInt(3);

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(24);
			DataOutputStream outputStream = new DataOutputStream(bos);
			try {
				outputStream.writeInt(WorldX);
				outputStream.writeInt(WorldY);
				outputStream.writeInt(WorldZ);
				outputStream.writeInt(DimensionNo);
				outputStream.writeInt(BlockID);
				outputStream.writeInt(SeedID);
				outputStream.writeInt(WheatID);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "anaso_WH";
			packet.data = bos.toByteArray();
			packet.length = bos.size();

			Side side = FMLCommonHandler.instance().getEffectiveSide();
			if(side == Side.CLIENT)
			{
				Minecraft MC = ModLoader.getMinecraftInstance();

				PacketDispatcher.sendPacketToServer(packet);
				MC.sndManager.playSoundFX("dig.grass", MC.gameSettings.soundVolume, 1.0F);
				Check = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return Check;
	}
}