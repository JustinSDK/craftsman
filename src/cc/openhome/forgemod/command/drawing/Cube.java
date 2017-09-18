package cc.openhome.forgemod.command.drawing;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public int lengthOfArgs() {
        return 6;
    }
    
    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {     
        Commons.runIfAirOrBlockHeld(sender, () -> {
            doCommandWithoutCheckingBlock(sender, args); 
        });
    }

    public void doCommandWithoutCheckingBlock(ICommandSender sender, String[] args) {
        Map<String, Integer> argsInt = Commons.argsToInteger(
                new String[] {"ux", "uy", "uz", "rows", "columns", "layers"}, 
                args
        );
        
        EntityPlayer player = (EntityPlayer) sender;
        Item heldItem = player.getHeldItemMainhand().getItem();
        
        // Player is always regarded as facing to EAST from 1st person perspective. 

        BlockPos origin = Commons.origin(
            player, 
            argsInt.get("ux"), 
            argsInt.get("uy"), 
            argsInt.get("uz")
         );
        
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
                argsInt.get("rows"), argsInt.get("columns"), argsInt.get("layers")
            );
    }
}
