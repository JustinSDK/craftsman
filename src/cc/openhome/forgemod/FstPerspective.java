package cc.openhome.forgemod;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class FstPerspective {
	public final int rows;
	public final int columns;
	public final int height;
	
	public FstPerspective(int rows, int columns, int height) {
		this.rows = rows;
		this.columns = columns;
		this.height = height;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columns;
		result = prime * result + height;
		result = prime * result + rows;
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
		if (height != other.height)
			return false;
		if (rows != other.rows)
			return false;
		return true;
	}
	
}
