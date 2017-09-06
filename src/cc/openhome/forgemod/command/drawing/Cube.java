package cc.openhome.forgemod.command.drawing;

import static cc.openhome.forgemod.FstPerspective.Vertical.DOWN;
import static cc.openhome.forgemod.FstPerspective.Vertical.UP;

import java.util.Arrays;
import java.util.List;

import cc.openhome.forgemod.Blocker;
import cc.openhome.forgemod.Commons;
import cc.openhome.forgemod.FstPerspective;
import cc.openhome.forgemod.Position;
import cc.openhome.forgemod.command.DefaultCommand;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
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
        Commons.runIfBlockHeld(sender, () -> {
            EntityPlayer player = (EntityPlayer) sender;
            Item heldItem = player.getHeldItemMainhand().getItem();

            // Player is always regarded as facing to EAST from 1st person perspective. 

            BlockPos origin = Commons.origin(
                player, 
                Integer.parseInt(args[0]), 
                Integer.parseInt(args[1]), 
                Integer.parseInt(args[2])
             );

            
            FstPerspective perspective = new FstPerspective(
                    UP, 
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]), 
                    Integer.parseInt(args[5])
                );
            
            Blocker.cubeWith(
                    player.getAdjustedHorizontalFacing(), 
                    origin, 
                    pos -> {
                        player.getEntityWorld().setBlockState(pos, Block.getBlockFromItem(heldItem).getDefaultState());
                    }, 
                    perspective
                ); 
        });
    }
}
