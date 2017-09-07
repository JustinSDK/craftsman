package cc.openhome.forgemod.command;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class FstPerspective {
    public final EnumFacing vt;
    public final int rows;
    public final int columns;
    public final int layers;

    public FstPerspective(EnumFacing vt, int rows, int columns, int layers) {
        this.vt = vt;
        this.rows = rows;
        this.columns = columns;
        this.layers = layers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + columns;
        result = prime * result + layers;
        result = prime * result + rows;
        result = prime * result + ((vt == null) ? 0 : vt.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FstPerspective other = (FstPerspective) obj;
        if (columns != other.columns)
            return false;
        if (layers != other.layers)
            return false;
        if (rows != other.rows)
            return false;
        if (vt != other.vt)
            return false;
        return true;
    }


}
