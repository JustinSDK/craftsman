package cc.openhome.forgemod.command;

import java.util.ArrayList;
import java.util.List;

import cc.openhome.forgemod.Blocker;
import cc.openhome.forgemod.FstPerspective;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Arrays;

public class DestroyCommand extends CubeCommand {
	@Override
	public String getName() {
		return "destroy";
	}

	@Override
	public void doCommand(MinecraftServer server, ICommandSender sender, FstPerspective cube) throws CommandException {
		EntityPlayer player = (EntityPlayer) sender;
		
		Blocker.cubeWith(
			player.getAdjustedHorizontalFacing(),
			player.getPosition(),
			pos -> player.getEntityWorld().destroyBlock(pos, true), 
			cube
	    );
	}
}
