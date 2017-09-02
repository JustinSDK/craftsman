package cc.openhome.forgemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public 	interface Blocker {
	void setBlock(BlockPos pos);
	
	public static void cubeWith(EnumFacing facing, BlockPos pos, Blocker blocker, FstPerspective perspective) {
		for(int layer = 0; layer < perspective.layers; layer++) {
			rectangleWith(
				facing, 
				pos.add(0, layer, 0), 
				blocker, 
				perspective.rows, 
				perspective.columns
			);
		}
	}	

	public static void rectangleWith(EnumFacing facing, BlockPos pos, Blocker blocker, int rows, int columns) {
		for(int column = 0; column < columns; column++) {
			columnWith(
				facing, 
				Position.right(facing, pos, column),
				blocker, rows
			);
		}		
	}
	
	public static void columnWith(EnumFacing facing, BlockPos pos, Blocker blocker, int length) {
		for(int i = 0; i < length; i++) {
			pos = Position.forward(facing, pos, 1);
			blocker.setBlock(pos);
		}		
	}
}
