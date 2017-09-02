package cc.openhome.forgemod.command;

import cc.openhome.forgemod.Blocker;
import cc.openhome.forgemod.FstPerspective;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class DigCommand extends CubeCommand {

	@Override
	public String getName() {
		return "dig";
	}

	@Override
	public void doCommand(MinecraftServer server, ICommandSender sender, FstPerspective perspective)
			throws CommandException {
		
		EntityPlayer player = (EntityPlayer) sender;
		
		Blocker.cubeWith(
			player.getAdjustedHorizontalFacing(),
			player.getPosition().add(0, perspective.layers, 0),
			pos -> player.getEntityWorld().destroyBlock(pos, true), 
			perspective
	    );
	}
}
