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
import cc.openhome.forgemod.command.drawing.Cube;

public class Pyramid implements DefaultCommand {
    @Override
    public String getName() {
        return "pyramid";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <ux> <uy> <uz> <width> <height>", getName());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;

        if (args.length != 5) {
            player.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }
        
        int ux = Integer.parseInt(args[0]);
        int uy = Integer.parseInt(args[1]);
        int uz = Integer.parseInt(args[2]);
        
        int width = Integer.parseInt(args[3]);
        int height = Integer.parseInt(args[4]);
        
        buildPyramid(player, ux, uy, uz, width, height);
    }

    private void buildPyramid(EntityPlayer player, int ux, int uy, int uz, int width, int height) {
        EnumFacing facing = player.getAdjustedHorizontalFacing();
        BlockPos playerPos = player.getPosition();
        Item heldItem = player.getHeldItemMainhand().getItem();
        
        Cube cube = new Cube();

        for (int h = 0; h < height; h++) {
            int w = width - h * 2;

            if (w <= 0) {
                break;
            }
            
            cube.doCommand(player, 
                    new String[] {
                            String.valueOf(ux + h), String.valueOf(uy + h), String.valueOf(uz + h),
                            String.valueOf(w), String.valueOf(w), "1"
                    }
            );
        }
    }
}
