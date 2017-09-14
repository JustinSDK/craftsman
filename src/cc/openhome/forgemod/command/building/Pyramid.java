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
import cc.openhome.forgemod.command.Blocker;
import cc.openhome.forgemod.command.Commons;
import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstDimension;
import cc.openhome.forgemod.command.FstPos;
import cc.openhome.forgemod.command.Walker;
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
        
        Commons.runIfAirOrBlockHeld(sender, () -> {
            buildPyramid(player, 
                new FstPos(ux, uy, uz), 
                new FstDimension(width, height)
            );
        });
    }

    private void buildPyramid(EntityPlayer player, FstPos fstPos, FstDimension fstDimension) {
        EnumFacing facing = player.getAdjustedHorizontalFacing();
        BlockPos playerPos = player.getPosition();
        Item heldItem = player.getHeldItemMainhand().getItem();
        
        Cube cube = new Cube();

        for (int h = 0; h < fstDimension.layers; h++) {
            int w = fstDimension.rows - h * 2;

            if (w <= 0) {
                break;
            }
            
            cube.doCommandWithoutCheckingBlock(player, 
                new String[] {
                        String.valueOf(fstPos.ux + h), String.valueOf(fstPos.uy + h), String.valueOf(fstPos.uz + h),
                        String.valueOf(w), String.valueOf(w), "1"
                }
            );
        }
    }
}
