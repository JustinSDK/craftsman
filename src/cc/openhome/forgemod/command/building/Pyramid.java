package cc.openhome.forgemod.command.building;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import cc.openhome.forgemod.Blocker;
import cc.openhome.forgemod.FstPerspective;
import cc.openhome.forgemod.Commons;
import cc.openhome.forgemod.Position;
import cc.openhome.forgemod.command.DefaultCommand;

public class Pyramid implements DefaultCommand {
    @Override
    public String getName() {
        return "pyramid";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <width> <height>", getName());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;

        if (args.length != 2) {
            player.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }
        
        Commons.runIfBlockHeld(sender, () -> {
            BlockPos playerPos = player.getPosition();

            int width = Integer.parseInt(args[0]);
            int height = Integer.parseInt(args[1]);

            buildPyramid(player, width, height); 
        });
    }

    private void buildPyramid(EntityPlayer player, int width, int height) {
        EnumFacing facing = player.getAdjustedHorizontalFacing();
        BlockPos playerPos = player.getPosition();
        Item heldItem = player.getHeldItemMainhand().getItem();

        for (int h = 0; h < height; h++) {
            int w = width - h * 2;

            if (w <= 0) {
                break;
            }

            Blocker.rectangleWith(
                facing, 
                new Position(facing, playerPos).forward(h + 1).right(h).up(h).getBlockPos(),
                pos -> {
                    player.getEntityWorld().setBlockState(pos, Block.getBlockFromItem(heldItem).getDefaultState());
                }, 
                w, w
            );
        }
    }
}