package cc.openhome.forgemod.command.drawing;

import java.util.Arrays;

import java.util.List;

import cc.openhome.forgemod.Commons;
import cc.openhome.forgemod.Position;
import cc.openhome.forgemod.command.DefaultCommand;
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

public class Turtle implements DefaultCommand {
    @Override
    public String getName() {
        return "turtle";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <on|off>", getName());
    }

    private class TurtleHandler {
        @SubscribeEvent
        public void track(PlayerTickEvent event) {
            EntityPlayer player = event.player;
            
            Item heldItem = player.getHeldItemMainhand().getItem();
            if (heldItem.equals(Items.AIR) || !(heldItem instanceof ItemBlock)) {
                return;
            }    
            
            if(player.isSneaking()) {
                BlockPos playerPos = 
                        Position.forward(player.getAdjustedHorizontalFacing(), player.getPosition(), 1);            
                player.setPosition(playerPos.getX(), playerPos.getY() - 0.5, playerPos.getZ());
            } 
            
            IBlockState heldBlockState = Block.getBlockFromItem(heldItem).getDefaultState();
            BlockPos basePos = player.getPosition().add(0, -1, 0);
            IBlockState baseState = player.getEntityWorld().getBlockState(basePos);
            
            if (heldBlockState.equals(baseState)) {
                return;
            }

            player.getEntityWorld().setBlockState(basePos, heldBlockState);
        }
    }

    private TurtleHandler trutle = new TurtleHandler();

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            Commons.sendMessageTo((EntityPlayer) sender, getUsage(sender));
        }

        if ("on".equals(args[0])) {
            MinecraftForge.EVENT_BUS.register(trutle);
        } else {
            MinecraftForge.EVENT_BUS.unregister(trutle);
        }
    }
}
