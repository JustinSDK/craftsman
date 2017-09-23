package cc.openhome.forgemod.command.drawing;


import static cc.openhome.forgemod.command.Args.*;

import java.util.Map;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstPlayer;
import cc.openhome.forgemod.command.FstPos;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractCircle implements DefaultCommand {
    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <ht|vt> <ux> <uy> <uz> <radius>", getName());
    }

    @Override
    public int minLengthOfArgs() {
        return 5;
    }

    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {        
        FstPlayer player = new FstPlayer(sender);
        
        player.runIfAirOrBlockHeld(() -> {
            Map<String, Integer> argsInt = argsToInteger(copyArgs(args, 1), 
                    "ux", "uy", "uz", "radius");
            
            FstPos center = new FstPos(
                    argsInt.get("ux"),
                    argsInt.get("uy"),
                    argsInt.get("uz")
                );
            
            if("vt".equals(args[0])) {
                buildVerticalCircle(player, center, argsInt.get("radius"));
            } else {
                buildHorizontalCircle(player, center, argsInt.get("radius"));
            }
        });
    }
    
    protected abstract void buildVerticalCircle(FstPlayer player, FstPos center, int radius);
    protected abstract void buildHorizontalCircle(FstPlayer player, FstPos center, int radius);
}
