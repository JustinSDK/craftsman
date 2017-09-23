package cc.openhome.forgemod.command.drawing;

import cc.openhome.forgemod.command.FstPlayer;
import cc.openhome.forgemod.command.FstPos;
import net.minecraft.entity.player.EntityPlayer;

public class FillCircle extends AbstractCircle {
    @Override
    public String getName() {
        return "fillCircle";
    }

    @Override
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
        
        for(int i = radius; i >= -radius; i--) {
            player.buildHeldBlock(new FstPos(x0, y0, z0 + i));
        }
        
        while(z < y) {
            if(f >= 0) {
                y -= 1;
                ddf_y += 2;
                f += ddf_y;
            }
            z += 1;
            ddf_z += 2;
            f += ddf_z;  
            
            for(int i = z; i >= -z; i--) {
                player.buildHeldBlock(new FstPos(x0, y0 + y, z0 + i));
                player.buildHeldBlock(new FstPos(x0, y0 - y, z0 + i));
            }
            
            for(int i = y; i >= -y; i--) {
                player.buildHeldBlock(new FstPos(x0, y0 + z, z0 + i));
                player.buildHeldBlock(new FstPos(x0, y0 - z, z0 + i));
            }
        }        
    }

    @Override
    protected void buildHorizontalCircle(FstPlayer player, FstPos center, int radius) {
        int x0 = center.ux;
        int y0 = center.uy;
        int z0 = center.uz;
        
        int f = 1 - radius;
        int ddf_x = 1;
        int ddf_z = -2 * radius;
        int x = 0;
        int z = radius;
        
        for(int i = radius; i >= -radius; i--) {
            player.buildHeldBlock(new FstPos(x0, y0, z0 + i));
        }
        
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

            for(int i = z; i >= -z; i--) {
                player.buildHeldBlock(new FstPos(x0 + x, y0, z0 + i));
                player.buildHeldBlock(new FstPos(x0 - x, y0, z0 + i));
            }
            
            for(int i = x; i >= -x; i--) {
                player.buildHeldBlock(new FstPos(x0 + z, y0, z0 + i));
                player.buildHeldBlock(new FstPos(x0 - z, y0, z0 + i));
            }
        }        
    }

}
