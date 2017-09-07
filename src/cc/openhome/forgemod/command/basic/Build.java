package cc.openhome.forgemod.command.basic;

import java.util.Arrays;

import java.util.List;

import cc.openhome.forgemod.command.Blocker;
import cc.openhome.forgemod.command.Commons;
import cc.openhome.forgemod.command.FstPerspective;
import cc.openhome.forgemod.command.Position;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class Build implements RCLbasedCommand {
    @Override
    public String getName() {
        return "build";
    }

    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, FstPerspective perspective)
            throws CommandException {

        
        Commons.runIfBlockHeld(sender, () -> {
            EntityPlayer player = (EntityPlayer) sender;
            Item heldItem = player.getHeldItemMainhand().getItem();
            
            Position position = new Position(
                player.getAdjustedHorizontalFacing(), 
                origin(player, perspective.vt, perspective.layers)
            );
            
            Blocker.cubeWith(
                    position,  
                    pos -> {
                        player.getEntityWorld().setBlockState(pos, Block.getBlockFromItem(heldItem).getDefaultState());
                    }, 
                    perspective.rows, perspective.columns, perspective.layers
                );            
        });

    }
}
