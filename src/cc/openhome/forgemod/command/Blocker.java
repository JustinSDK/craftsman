package cc.openhome.forgemod.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface Blocker {
    void setBlock(BlockPos pos);

    public static void cubeWith(Walker position, Blocker blocker, int rows, int columns, int layers) {
        for (int layer = 0; layer < layers; layer++) {
            rectangleWith(position.up(layer), blocker, rows, columns);
        }
    }
    
    public static void rectangleWith(Walker position, Blocker blocker, int rows, int columns) {
        for (int column = 0; column < columns; column++) {
            columnWith(position.right(column), blocker, rows);
        }
    }    
    
    public static void columnWith(Walker position, Blocker blocker, int length) {
        Walker pos = position;
        for (int i = 0; i < length; i++) {
            blocker.setBlock(pos.getBlockPos());
            pos = pos.forward(1);
        }
    }    
}
