package anaso.WheatHarvest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;
import javax.swing.text.html.parser.Entity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity)
	{
		 if(packet.channel.equals("anaso_WH"))
		 {
			 HandlePacket(packet);
		 }
	}

	public void HandlePacket(Packet250CustomPayload packet)
	{
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

		int BlockX;
		int BlockY;
		int BlockZ;
		int Dimension;
		int BlockID;
		int SeedID;
		int WheatID;

		try {
			BlockX = inputStream.readInt();
			BlockY = inputStream.readInt();
			BlockZ = inputStream.readInt();
			Dimension = inputStream.readInt();
			BlockID = inputStream.readInt();
			SeedID = inputStream.readInt();
			WheatID = inputStream.readInt();
		}

		catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Random Rand = new Random(System.currentTimeMillis());
		int RandInt = Rand.nextInt(3);

		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT)
		{
			//ModLoader.getMinecraftInstance().renderGlobal.loadRenderers();
			//System.out.println("PlaySound");
		}
		else if(side == Side.SERVER)
		{
			MinecraftServer MC = ModLoader.getMinecraftServerInstance();
			//MC.worldServers[Dimension].setBlockAndMetadataWithNotify(BlockX, BlockY, BlockZ, BlockID, 0, 0);
			MC.worldServers[Dimension].setBlockMetadataWithNotify(BlockX, BlockY, BlockZ, 0, 0);

			if(RandInt != 0)
			{
				EntityItem spawnSeedEntity = new EntityItem(MC.worldServers[Dimension], BlockX, BlockY+1, BlockZ, new ItemStack(Item.itemsList[SeedID], RandInt));
				MC.worldServers[Dimension].spawnEntityInWorld(spawnSeedEntity);
			}
			if(SeedID == Item.seeds.itemID)
			{
				EntityItem spawnWheatEntity = new EntityItem(MC.worldServers[Dimension], BlockX, BlockY+1, BlockZ, new ItemStack(Item.itemsList[WheatID], 1));
				MC.worldServers[Dimension].spawnEntityInWorld(spawnWheatEntity);
			}
			else if(SeedID == Item.potato.itemID && Rand.nextInt(50) == 0)
			{
				EntityItem spawnWheatEntity = new EntityItem(MC.worldServers[Dimension], BlockX, BlockY+1, BlockZ, new ItemStack(Item.poisonousPotato, 1));
				MC.worldServers[Dimension].spawnEntityInWorld(spawnWheatEntity);
			}
		}
		else
		{
			// We are on the Bukkit server.
		}
	}
}