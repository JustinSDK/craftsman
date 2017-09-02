package cc.openhome.forgemod.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import cc.openhome.forgemod.Blocker;
import cc.openhome.forgemod.FstPerspective;
import cc.openhome.forgemod.Messenger;
import cc.openhome.forgemod.Position;

public class PyramidCommand implements ICommand {
	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getName() {
		return "pyramid";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return String.format("/%s <width> <height>", getName());
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(getName());
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender; 
		
		if(args.length != 2) {
			player.sendMessage(
				new TextComponentString(getUsage(sender))
			);
			return;
		}

		Item heldItem = player.getHeldItemMainhand().getItem();
		if(heldItem.equals(Items.AIR) || !(heldItem instanceof ItemBlock)) {
			Messenger.sendMessageTo(player, "Select a block");
			return;
		}		
		
		
		BlockPos playerPos = player.getPosition();
		
	    int width = Integer.parseInt(args[0]);
	    int height = Integer.parseInt(args[1]);
	    
	    buildPyramid(player, width, height);
	}

	private void buildPyramid(EntityPlayer player, int width, int height) {		
		EnumFacing facing = player.getAdjustedHorizontalFacing();
		
		BlockPos playerPos = player.getPosition(); 
		Item heldItem = player.getHeldItemMainhand().getItem();
		
		for(int h = 0; h < height; h++) {
			int w = width - h * 2;
			
			if(w <= 0) {
				break;
			}
			
			Blocker.buildRectangle(
				facing, 
				new Position(facing, playerPos).forward(h).right(h).up(h).getBlockPos(),
				pos -> {
					player.getEntityWorld().setBlockState(
							pos, 
							Block.getBlockById(Item.getIdFromItem(heldItem)).getBlockState().getBaseState()
					);
				}, 
				w, w
			);
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
