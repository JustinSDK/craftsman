package cc.openhome.forgemod.command.drawing;

import static cc.openhome.forgemod.command.Commons.argsToInteger;
import static cc.openhome.forgemod.command.Commons.buildHeldBlock;
import static cc.openhome.forgemod.command.Commons.copyArgs;
import static cc.openhome.forgemod.command.Commons.runIfAirOrBlockHeld;

import java.util.Map;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstPos;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class Sphere implements DefaultCommand {
    @Override
    public String getName() {
        return "sphere";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <solid|hollow> <ux> <uy> <uz> <radius>", getName());
    }

    @Override
    public int minLengthOfArgs() {
        return 5;
    }

    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        runIfAirOrBlockHeld(sender, () -> {
            Map<String, Integer> argsInt = argsToInteger(
                    new String[] {"ux", "uy", "uz", "radius"}, 
                    copyArgs(args, 1)
            );
            
            EntityPlayer player = (EntityPlayer) sender;
            FstPos center = new FstPos(
                    argsInt.get("ux"),
                    argsInt.get("uy"),
                    argsInt.get("uz")
                );
            
            if("solid".equals(args[0])) {
                sphere(player, center, argsInt.get("radius"));
            } else {
                hollowSphere(player, center, argsInt.get("radius"));
            }
        });
    }

    private void sphere(EntityPlayer player, FstPos center, int radius) {
        int x0 = center.ux;
        int y0 = center.uy;
        int z0 = center.uz;
        
        int powR = radius * radius;
        
        for(int x = -radius; x < radius; x++) {
            for(int y = -radius; y < radius; y++) {
                for(int z = -radius; z < radius; z++) {
                    if(x * x + y * y + z * z < powR) {
                        buildHeldBlock(new FstPos(x0 + x, y0 + y, z0 + z), player);
                    }
                }
            }
        }
    }
    
    private void hollowSphere(EntityPlayer player, FstPos center, int radius) {
        int x0 = center.ux;
        int y0 = center.uy;
        int z0 = center.uz;
        
        int powR = radius * radius;
        int doubleR = radius * 2;
        
        for(int x = -radius; x < radius; x++) {
            for(int y = -radius; y < radius; y++) {
                for(int z = -radius; z < radius; z++) {
                    int lenPow = x * x + y * y + z * z;
                    if(lenPow < powR && lenPow > powR - doubleR) {
                        buildHeldBlock(new FstPos(x0 + x, y0 + y, z0 + z), player);
                    }
                }
            }
        }
    }
}
