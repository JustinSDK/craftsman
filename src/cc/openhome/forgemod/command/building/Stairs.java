package cc.openhome.forgemod.command.building;

import static cc.openhome.forgemod.command.Commons.*;
import java.util.Map;


import cc.openhome.forgemod.command.Blocker;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.Walker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import scala.actors.threadpool.Arrays;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

public class Stairs implements DefaultCommand {

    @Override
    public String getName() {
        return "stairs";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <up|down> <ux> <uy> <uz> <height> <width>", getName());
    }

    public int minLengthOfArgs() {
        return 6;
    }
    
    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {     
        runIfAirOrStairsHeld(sender, () -> {     
            Map<String, Integer> argsInt = argsToInteger(
                new String[] {"ux", "uy", "uz", "height", "width"}, 
                copyArgs(args, 1)
            );

            int height = argsInt.get("height");
            int width = argsInt.get("width");
            
            EntityPlayer player = (EntityPlayer) sender;
            BlockPos origin = origin(player, argsInt.get("ux"), argsInt.get("uy"), argsInt.get("uz"));
            Item heldItem = player.getHeldItemMainhand().getItem();
            Block heldBlock = Block.getBlockFromItem(heldItem);
            
            if("up".equals(args[0])) {
                for(int w = 0; w < width; w++) {
                    BlockPos pos = Walker.right(player.getAdjustedHorizontalFacing(), origin, w);
                    columnUp(player, height, pos, heldBlock);
                }
            } else {
                for(int w = 0; w < width; w++) {
                    BlockPos pos = Walker.right(player.getAdjustedHorizontalFacing(), origin, w);
                    columnDown(player, height, pos, heldBlock);
                }
            }
        });
    }


    private void columnUp(EntityPlayer player, int height, BlockPos origin, Block heldBlock) {
        for(int h = 0; h < height; h++) {
            BlockPos pos = new Walker(player.getAdjustedHorizontalFacing(), origin)
                                  .forward(h).up(h).getBlockPos();
            IBlockState state = heldBlock.getDefaultState();
            
            player.getEntityWorld().setBlockState(
                    pos, 
                    state.withRotation(fromFacingForUp(player)) 
            );
        }
    }
    
    private Rotation fromFacingForUp(EntityPlayer player) {
        EnumFacing facing = player.getAdjustedHorizontalFacing();
        if(facing == EnumFacing.EAST) {
            return Rotation.CLOCKWISE_90;
        } 
        else if(facing == EnumFacing.WEST) {
            return Rotation.COUNTERCLOCKWISE_90;
        }
        else if(facing == EnumFacing.SOUTH) {
            return Rotation.CLOCKWISE_180;
        }
        else {
            return Rotation.NONE;
        }
    }


    private void columnDown(EntityPlayer player, int height, BlockPos origin, Block heldBlock) {
        for(int h = 0; h < height; h++) {
            BlockPos pos = new Walker(player.getAdjustedHorizontalFacing(), origin)
                                  .forward(h).up(-h - 1).getBlockPos();
            IBlockState state = heldBlock.getDefaultState();
            
            player.getEntityWorld().setBlockState(
                    pos, 
                    state.withRotation(fromFacingForDown(player)) 
            );
        }
    }
    
    private Rotation fromFacingForDown(EntityPlayer player) {
        EnumFacing facing = player.getAdjustedHorizontalFacing();
        if(facing == EnumFacing.EAST) {
            return Rotation.COUNTERCLOCKWISE_90;
        } 
        else if(facing == EnumFacing.WEST) {
            return Rotation.CLOCKWISE_90;
        }
        else if(facing == EnumFacing.NORTH) {
            return Rotation.CLOCKWISE_180;
        }
        else {
            return Rotation.NONE;
        }
    }    
}
