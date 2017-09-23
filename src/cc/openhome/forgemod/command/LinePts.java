package cc.openhome.forgemod.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class LinePts {
    private BlockPos start;
    private BlockPos end;
    
    public LinePts(BlockPos start, BlockPos end) {
        this.start = start;
        this.end = end;
    }
    
    private int ax;
    private int ay;
    private int az;
    
    private int sx;
    private int sy;
    private int sz;       
    
    public List<BlockPos> getList() {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        int dz = end.getZ() - start.getZ();
        
        ax = Math.abs(dx) << 1;
        ay = Math.abs(dy) << 1;
        az = Math.abs(dz) << 1;
        
        sx = zsgn(dx);
        sy = zsgn(dy);
        sz = zsgn(dz);        
        
        if(ax >= Math.max(ay, az)) {
            return xDominant(start, end);
        } else if(ay >= Math.max(ax, az)) {
            return yDominant(start, end);
        } 
        return zDominant(start, end);
    }

    private List<BlockPos> xDominant(BlockPos start, BlockPos end) {
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        
        int yd = ay - (ax >> 1);
        int zd = az - (ax >> 1);
        
        List<BlockPos> posLt = new ArrayList<>();
        do {
            posLt.add(new BlockPos(x, y, z));
            if(yd >= 0) {
                y += sy;
                yd -= ax;
            }
            if(zd >= 0) {
                z += sz;
                zd -= ax;                    
            }
            x += sx;
            yd += ay;
            zd += az;
        } while(x != end.getX());
        return posLt;
    }

    private List<BlockPos> yDominant(BlockPos start, BlockPos end) {
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        
        int xd = ax - (ay >> 1);
        int zd = az - (ay >> 1);
        
        List<BlockPos> posLt = new ArrayList<>();
        do {
            posLt.add(new BlockPos(x, y, z));
            if(xd >= 0) {
                x += sx;
                xd -= ay;
            }
            if(zd >= 0) {
                z += sz;
                zd -= ay;                    
            }
            y += sy;
            xd += ax;
            zd += az;
        } while(y != end.getY());
        return posLt;
    }
    
    private List<BlockPos> zDominant(BlockPos start, BlockPos end) {
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        
        int xd = ax - (az >> 1);
        int yd = ay - (az >> 1);
        
        List<BlockPos> posLt = new ArrayList<>();
        do {
            posLt.add(new BlockPos(x, y, z));
            if(xd >= 0) {
                x += sx;
                xd -= az;
            }
            if(yd >= 0) {
                y += sy;
                yd -= az;                    
            }
            z += sz;
            xd += ax;
            yd += ay;
        } while(z != end.getZ());
        return posLt;
    }    
    
    private int zsgn(int a) {
        if(a < 0) {
            return -1;
        } 
        else if(a > 0) {
            return 1;
        }
        return 0;
    }
}
