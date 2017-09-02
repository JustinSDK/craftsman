package cc.openhome.forgemod.command;

import java.util.Arrays;
import java.util.List;

import cc.openhome.forgemod.Blocker;
import cc.openhome.forgemod.FstPerspective;
import cc.openhome.forgemod.Messenger;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class BuildCommand extends CubeCommand {
	@Override
	public String getName() {
		return "build";
	}
	
	@Override
	public void doCommand(MinecraftServer server, ICommandSender sender, FstPerspective perspective) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender;
		
		Item heldItem = player.getHeldItemMainhand().getItem();
		if(heldItem.equals(Items.AIR) || !(heldItem instanceof ItemBlock)) {
			Messenger.sendMessageTo(player, "Select a block");
			return;
		}
		
		Blocker.buildCube(
			player, 
			pos -> {
				player.getEntityWorld().setBlockState(
						pos, 
						Block.getBlockById(Item.getIdFromItem(heldItem)).getBlockState().getBaseState()
				);
			},
			perspective
	    );		
	}
}