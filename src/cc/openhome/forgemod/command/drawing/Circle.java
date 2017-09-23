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

public class Circle extends AbstractCircle {
    @Override
    public String getName() {
        return "circle";
    }
    
    protected void buildVerticalCircle(FstPlayer player, FstPos center, int radius) {
        int x0 = center.ux;
        int y0 = center.uy;
        int z0 = center.uz;
        
        int f = 1 - radius;
        int ddf_z = 1;
        int ddf_y = -2 * radius;
        int z = 0;
        int y = radius;
        
        player.buildHeldBlock(new FstPos(x0, y0 + radius, z0));
        player.buildHeldBlock(new FstPos(x0, y0 - radius, z0));
        player.buildHeldBlock(new FstPos(x0, y0, z0 + radius));
        player.buildHeldBlock(new FstPos(x0, y0, z0 - radius));
        
        while(z < y) {
            if(f >= 0) {
                y -= 1;
                ddf_y += 2;
                f += ddf_y;
            }
            z += 1;
            ddf_z += 2;
            f += ddf_z;  
            
            player.buildHeldBlock(new FstPos(x0, y0 + y, z0 + z));
            player.buildHeldBlock(new FstPos(x0 , y0 + y, z0 - z));
            player.buildHeldBlock(new FstPos(x0, y0 - y, z0 + z));
            player.buildHeldBlock(new FstPos(x0, y0 - y, z0 - z));
            player.buildHeldBlock(new FstPos(x0, y0 + z, z0 + y));
            player.buildHeldBlock(new FstPos(x0, y0 + z, z0 - y));
            player.buildHeldBlock(new FstPos(x0, y0 - z, z0 + y));
            player.buildHeldBlock(new FstPos(x0, y0 - z, z0 - y));
        }
    }
    
    protected void buildHorizontalCircle(FstPlayer player, FstPos center, int radius) {
        int x0 = center.ux;
        int y0 = center.uy;
        int z0 = center.uz;
        
        int f = 1 - radius;
        int ddf_x = 1;
        int ddf_z = -2 * radius;
        int x = 0;
        int z = radius;
        
        player.buildHeldBlock(new FstPos(x0, y0, z0 + radius));
        player.buildHeldBlock(new FstPos(x0, y0, z0 - radius));
        player.buildHeldBlock(new FstPos(x0 + radius, y0, z0));
        player.buildHeldBlock(new FstPos(x0 - radius, y0, z0));
        
        while(x < z) {
            if(f >= 0) {
                z -= 1;
                ddf_z += 2;
                f += ddf_z;
            }
            x += 1;
            ddf_x += 2;
            f += ddf_x;

            player.buildHeldBlock(new FstPos(x0 + x, y0, z0 + z));
            player.buildHeldBlock(new FstPos(x0 - x, y0, z0 + z));
            player.buildHeldBlock(new FstPos(x0 + x, y0, z0 - z));
            player.buildHeldBlock(new FstPos(x0 - x, y0, z0 - z));
            player.buildHeldBlock(new FstPos(x0 + z, y0, z0 + x));
            player.buildHeldBlock(new FstPos(x0 - z, y0, z0 + x));
            player.buildHeldBlock(new FstPos(x0 + z, y0, z0 - x));
            player.buildHeldBlock(new FstPos(x0 - z, y0, z0 - x));
        }
    }    
}
