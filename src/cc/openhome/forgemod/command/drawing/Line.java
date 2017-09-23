package cc.openhome.forgemod.command.drawing;

import static cc.openhome.forgemod.command.Commons.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstDimension;
import cc.openhome.forgemod.command.FstPos;
import cc.openhome.forgemod.command.FstWalker;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class Line implements DefaultCommand {

    @Override
    public String getName() {
        return "line";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <ux1> <uy1> <uz1> <ux2> <uy2> <uz2>", getName());
    } 

    public int minLengthOfArgs() {
        return 6;
    }
    
    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {             
        runIfAirOrBlockHeld(sender, () -> {
            Map<String, Integer> argsInt = argsToInteger(
                    new String[] {"ux1", "uy1", "uz1", "ux2", "uy2", "uz2"}, 
                    args
            );            
            
            EntityPlayer player = (EntityPlayer) sender;

            BlockPos start = toBlockPos(new FstPos(
                    argsInt.get("ux1"),
                    argsInt.get("uy1"),
                    argsInt.get("uz1")
                ), player);
            
            BlockPos end = toBlockPos(new FstPos(
                    argsInt.get("ux2"),
                    argsInt.get("uy2"),
                    argsInt.get("uz2")
                ), player);      
            
            
            new LinePts(start, end).getList().forEach(pos -> buildHeldBlock(pos, player));
        });
    }
}
