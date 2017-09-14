package cc.openhome.forgemod.command;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class FstDimension {
    public final int rows;
    public final int columns;
    public final int layers;

    public FstDimension(int rows, int columns, int layers) {
        this.rows = rows;
        this.columns = columns;
        this.layers = layers;
    }
    
    public FstDimension(int width, int height) {
        this.rows = width;
        this.columns = width;
        this.layers = height;
    }    
}
