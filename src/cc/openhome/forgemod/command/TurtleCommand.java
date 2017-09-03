package cc.openhome.forgemod.command;


import java.util.Arrays;
import java.util.List;

import cc.openhome.forgemod.Messenger;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TurtleCommand implements ICommand {
	 private class TurtleHandler {
	    @SubscribeEvent
	    public void track(PlayerTickEvent event) {
			EntityPlayer player = event.player;
			
			IBlockState heldBlockState = Block.getBlockFromItem(
										    player.getHeldItemMainhand().getItem()
										 ).getDefaultState();
			
			BlockPos basePos = player.getPosition().add(0, -1, 0);
			IBlockState baseState = player.getEntityWorld().getBlockState(basePos);
			
			if(heldBlockState.equals(baseState)) {
				return;
			}
			
			player.getEntityWorld().setBlockState(basePos, heldBlockState);
	    } 
	}
	 
	private TurtleHandler trutle = new TurtleHandler();

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getName() {
		return "turtle";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return String.format("/%s <on|off>", getName());
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(getName());
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length != 1) {
			Messenger.sendMessageTo((EntityPlayer) sender, getUsage(sender));
		}
		
		if("on".equals(args[0])) {
			MinecraftForge.EVENT_BUS.register(trutle);
		} else {
			MinecraftForge.EVENT_BUS.unregister(trutle);
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
