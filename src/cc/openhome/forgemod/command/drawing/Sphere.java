package cc.openhome.forgemod.command.drawing;

import static cc.openhome.forgemod.command.Args.argsToInteger;
import static cc.openhome.forgemod.command.Args.copyArgs;

import java.util.Map;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstPlayer;
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
        FstPlayer player = new FstPlayer(sender);
        
        player.runIfAirOrBlockHeld(() -> {
            Map<String, Integer> argsInt = argsToInteger(copyArgs(args, 1), 
                    "ux", "uy", "uz", "radius"
            );
            
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

    private void sphere(FstPlayer player, FstPos center, int radius) {
        int x0 = center.ux;
        int y0 = center.uy;
        int z0 = center.uz;
        
        int powR = radius * radius;
        
        for(int x = -radius; x < radius; x++) {
            for(int y = -radius; y < radius; y++) {
                for(int z = -radius; z < radius; z++) {
                    if(x * x + y * y + z * z < powR) {
                        player.buildHeldBlock(new FstPos(x0 + x, y0 + y, z0 + z));
                    }
                }
            }
        }
    }
    
    private void hollowSphere(FstPlayer player, FstPos center, int radius) {
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
                        player.buildHeldBlock(new FstPos(x0 + x, y0 + y, z0 + z));
                    }
                }
            }
        }
    }
}
