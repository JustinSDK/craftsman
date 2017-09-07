package cc.openhome.forgemod.command.basic;

import java.util.Arrays;

import java.util.List;

import cc.openhome.forgemod.command.Commons;
import cc.openhome.forgemod.command.DefaultCommand;
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

public interface RCLbasedCommand extends DefaultCommand {
    @Override
    default String getUsage(ICommandSender sender) {
        return String.format("/%s <up|down> <rows> <columns> <layers>", getName());
    }
    
    @Override
    default void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 4) {
            Commons.sendMessageTo(((EntityPlayer) sender), getUsage(sender));
            return;
        }

        FstPerspective perspective = new FstPerspective(
            args[0].equals("up") ? EnumFacing.UP : EnumFacing.DOWN, 
            Integer.parseInt(args[1]),
            Integer.parseInt(args[2]), 
            Integer.parseInt(args[3])
        );

        doCommand(server, sender, perspective);
    }
    
    default BlockPos origin(EntityPlayer player, EnumFacing vt, int layers) { 
        BlockPos origin = Position.forward(player.getAdjustedHorizontalFacing(), player.getPosition(), 1);
        return vt.equals(EnumFacing.UP) ? origin : origin.add(0, -layers, 0);
    }           
    
    void doCommand(MinecraftServer server, ICommandSender sender, FstPerspective perspective) throws CommandException;
}
