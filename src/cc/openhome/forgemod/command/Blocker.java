package cc.openhome.forgemod.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface Blocker {
    void setBlock(BlockPos pos);

    public static void cubeWith(FstWalker walker, Blocker blocker, int rows, int columns, int layers) {
        for (int layer = 0; layer < layers; layer++) {
            rectangleWith(walker.up(layer), blocker, rows, columns);
        }
    }
    
    public static void rectangleWith(FstWalker walker, Blocker blocker, int rows, int columns) {
        for (int column = 0; column < columns; column++) {
            columnWith(walker.right(column), blocker, rows);
        }
    }    
    
    public static void columnWith(FstWalker walker, Blocker blocker, int length) {
        FstWalker walkerVar = walker;
        for (int i = 0; i < length; i++) {
            blocker.setBlock(walkerVar.getBlockPos());
            walkerVar = walkerVar.forward(1);
        }
    }    
}
