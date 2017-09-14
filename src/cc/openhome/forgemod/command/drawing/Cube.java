package cc.openhome.forgemod.command.drawing;

import java.util.Arrays;
import java.util.List;

import cc.openhome.forgemod.command.Blocker;
import cc.openhome.forgemod.command.Commons;
import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstDimension;
import cc.openhome.forgemod.command.Walker;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class Cube implements DefaultCommand {
    @Override
    public String getName() {
        return "cube";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <ux> <uy> <uz> <rows> <columns> <layers>", getName());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 6) {
            Commons.sendMessageTo(((EntityPlayer) sender), getUsage(sender));
            return;
        }
        
        doCommand(sender, args);
    }

    public void doCommand(ICommandSender sender, String[] args) {
        Commons.runIfAirOrBlockHeld(sender, () -> {
            doCommandWithoutCheckingBlock(sender, args); 
        });
    }

    public void doCommandWithoutCheckingBlock(ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        Item heldItem = player.getHeldItemMainhand().getItem();

        // Player is always regarded as facing to EAST from 1st person perspective. 

        BlockPos origin = Commons.origin(
            player, 
            Integer.parseInt(args[0]), 
            Integer.parseInt(args[1]), 
            Integer.parseInt(args[2])
         );
        
        int rows = Integer.parseInt(args[3]);
        int columns = Integer.parseInt(args[4]); 
        int layers = Integer.parseInt(args[5]);
        
        Walker position = new Walker(
            player.getAdjustedHorizontalFacing(), 
            origin                    
        );
        
        Blocker.cubeWith(
                position, 
                pos -> {
                    player.getEntityWorld()
                          .setBlockState(pos, Block.getBlockFromItem(heldItem).getDefaultState());
                }, 
                rows, columns, layers
            );
    }
}
