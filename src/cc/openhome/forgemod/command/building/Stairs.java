package cc.openhome.forgemod.command.building;

import static cc.openhome.forgemod.command.Args.*;
import java.util.Map;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstPlayer;
import cc.openhome.forgemod.command.FstWalker;
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
        FstPlayer player = new FstPlayer(sender);
        
        player.runIfAirOrStairsHeld(() -> {     
            Map<String, Integer> argsInt = argsToInteger(
                new String[] {"ux", "uy", "uz", "height", "width"}, 
                copyArgs(args, 1)
            );

            int height = argsInt.get("height");
            int width = argsInt.get("width");
            
            BlockPos origin = player.origin(argsInt.get("ux"), argsInt.get("uy"), argsInt.get("uz"));
            
            if("up".equals(args[0])) {
                for(int w = 0; w < width; w++) {
                    BlockPos pos = FstWalker.right(player.getAdjustedHorizontalFacing(), origin, w);
                    columnUp(player, height, pos);
                }
            } else {
                for(int w = 0; w < width; w++) {
                    BlockPos pos = FstWalker.right(player.getAdjustedHorizontalFacing(), origin, w);
                    columnDown(player, height, pos);
                }
            }
        });
    }


    private void columnUp(FstPlayer player, int height, BlockPos origin) {
        for(int h = 0; h < height; h++) {
            BlockPos pos = new FstWalker(player.getAdjustedHorizontalFacing(), origin)
                                  .forward(h).up(h).getBlockPos();
            player.buildHeldBlock(pos, fromFacingForUp(player));
        }
    }
    
    private Rotation fromFacingForUp(FstPlayer player) {
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


    private void columnDown(FstPlayer player, int height, BlockPos origin) {
        for(int h = 0; h < height; h++) {
            BlockPos pos = new FstWalker(player.getAdjustedHorizontalFacing(), origin)
                                  .forward(h).up(-h - 1).getBlockPos();
            player.buildHeldBlock(pos, fromFacingForDown(player));
        }
    }
    
    private Rotation fromFacingForDown(FstPlayer player) {
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
