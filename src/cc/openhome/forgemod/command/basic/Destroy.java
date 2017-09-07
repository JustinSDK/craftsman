package cc.openhome.forgemod.command.basic;

import java.util.ArrayList;
import java.util.List;

import cc.openhome.forgemod.command.Blocker;
import cc.openhome.forgemod.command.Commons;
import cc.openhome.forgemod.command.FstPerspective;
import cc.openhome.forgemod.command.Position;
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

public class Destroy implements RCLbasedCommand {
    @Override
    public String getName() {
        return "destroy";
    }

    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, FstPerspective perspective)
            throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;
        
        Position position = new Position(
            player.getAdjustedHorizontalFacing(), 
            origin(player, perspective.vt, perspective.layers)
        );
            
        Blocker.cubeWith(
            position,
            pos -> player.getEntityWorld().destroyBlock(pos, true), 
            perspective.rows, perspective.columns, perspective.layers
        );
    }

}
