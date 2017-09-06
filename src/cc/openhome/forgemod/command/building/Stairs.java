package cc.openhome.forgemod.command.building;

import cc.openhome.forgemod.Blocker;
import cc.openhome.forgemod.Commons;
import cc.openhome.forgemod.Position;
import cc.openhome.forgemod.command.DefaultCommand;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
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

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 6) {
            Commons.sendMessageTo(((EntityPlayer) sender), getUsage(sender));
            return;
        }
        
        Commons.runIfStairsHeld(sender, () -> {           
            int ux = Integer.parseInt(args[1]);
            int uy = Integer.parseInt(args[2]);
            int uz = Integer.parseInt(args[3]);
            
            int height = Integer.parseInt(args[4]);
            int width = Integer.parseInt(args[5]);
            
            EntityPlayer player = (EntityPlayer) sender;
            BlockPos origin = Commons.origin(player, ux, uy, uz);
            Item heldItem = player.getHeldItemMainhand().getItem();
            BlockStairs heldBlock = (BlockStairs) Block.getBlockFromItem(heldItem);
            
            if("up".equals(args[0])) {
                for(int w = 0; w < width; w++) {
                    columnUp(player, height, Position.right(player.getAdjustedHorizontalFacing(), origin, w), heldBlock);
                }
            } else {
                for(int w = 0; w < width; w++) {
                    columnDown(player, height, Position.right(player.getAdjustedHorizontalFacing(), origin, w), heldBlock);
                }
            }
        });
    }


    private void columnUp(EntityPlayer player, int height, BlockPos origin, BlockStairs heldBlock) {
        for(int h = 0; h < height; h++) {
            BlockPos pos = new Position(player.getAdjustedHorizontalFacing(), origin)
                                  .forward(h).up(h).getBlockPos();
            player.getEntityWorld().setBlockState(
                    pos, 
                    heldBlock.getDefaultState().withRotation(fromFacingForUp(player))
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


    private void columnDown(EntityPlayer player, int height, BlockPos origin, BlockStairs heldBlock) {
        for(int h = 0; h < height; h++) {
            BlockPos pos = new Position(player.getAdjustedHorizontalFacing(), origin)
                                  .forward(h + 1).up(-h - 1).getBlockPos();
            player.getEntityWorld().setBlockState(
                    pos, 
                    heldBlock.getDefaultState().withRotation(fromFacingForDown(player))
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