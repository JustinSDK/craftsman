package cc.openhome.forgemod.command.building;

import static cc.openhome.forgemod.command.Args.*;
import java.util.Arrays;

import java.util.List;
import java.util.Map;

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
import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstDimension;
import cc.openhome.forgemod.command.FstPlayer;
import cc.openhome.forgemod.command.FstPos;
import cc.openhome.forgemod.command.FstWalker;
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
    public int minLengthOfArgs() {
        return 5;
    }
    
    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {             
        FstPlayer player = new FstPlayer(sender);
        
        player.runIfAirOrBlockHeld(() -> {
            Map<String, Integer> argsInt = argsToInteger(
                    new String[] {"ux", "uy", "uz", "width", "height"}, args);
            
            buildPyramid(player, 
                new FstPos(argsInt.get("ux"), argsInt.get("uy"), argsInt.get("uz")), 
                new FstDimension(argsInt.get("width"), argsInt.get("height"))
            );
        });
    }

    private void buildPyramid(FstPlayer player, FstPos fstPos, FstDimension fstDimension) {        
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
