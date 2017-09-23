package cc.openhome.forgemod.command.drawing;

import static cc.openhome.forgemod.command.Args.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstDimension;
import cc.openhome.forgemod.command.FstPlayer;
import cc.openhome.forgemod.command.FstWalker;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
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

    public int minLengthOfArgs() {
        return 6;
    }
    
    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {     
        FstPlayer player = new FstPlayer(sender);
        player.runIfAirOrBlockHeld(() -> {
            doCommandWithoutCheckingBlock(player, args); 
        });
    }

    public void doCommandWithoutCheckingBlock(FstPlayer player, String[] args) {
        Map<String, Integer> argsInt = argsToInteger(args, 
                "ux", "uy", "uz", "rows", "columns", "layers"
        );
        
        BlockPos origin = player.origin(
            argsInt.get("ux"), 
            argsInt.get("uy"), 
            argsInt.get("uz")
         );
        
        FstWalker walker = new FstWalker(
            player.getAdjustedHorizontalFacing(), 
            origin                    
        );
        
        buildCube(player, walker, argsInt.get("rows"), argsInt.get("columns"), argsInt.get("layers"));
    }
    
    private void buildCube(FstPlayer player, FstWalker walker, int rows, int columns, int layers) {
        for (int layer = 0; layer < layers; layer++) {
            buildRectangle(player, walker.up(layer), rows, columns);
        }
    }
    
    private void buildRectangle(FstPlayer player, FstWalker walker, int rows, int columns) {
        for (int column = 0; column < columns; column++) {
            buildColumn(player, walker.right(column), rows);
        }
    }    
    
    private void buildColumn(FstPlayer player, FstWalker walker, int length) {
        FstWalker walkerVar = walker;
        for (int i = 0; i < length; i++) {
            player.buildHeldBlock(walkerVar.getBlockPos());
            walkerVar = walkerVar.forward(1);
        }
    }        
}
