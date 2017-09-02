package cc.openhome.forgemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public 	interface Blocker {
	void setBlock(BlockPos pos);
	
	public static void buildCube(EnumFacing facing, BlockPos pos, Blocker blocker, FstPerspective perspective) {
		for(int h = 0; h < perspective.layers; h++) {
			buildRectangle(
				facing, 
				pos.add(0, h, 0), 
				blocker, 
				perspective.rows, 
				perspective.columns
			);
		}
	}	

	public static void buildRectangle(EnumFacing facing, BlockPos pos, Blocker blocker, int rows, int columns) {
		for(int column = 0; column < columns; column++) {
			buildColumn(
				facing, 
				Position.right(facing, pos, column),
				blocker, rows
			);
		}		
	}
	
	public static void buildColumn(EnumFacing facing, BlockPos pos, Blocker blocker, int length) {
		for(int i = 0; i < length; i++) {
			pos = Position.forward(facing, pos, 1);
			blocker.setBlock(pos);
		}		
	}
}
