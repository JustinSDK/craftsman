package cc.openhome.forgemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public 	interface Blocker {
	void setBlock(BlockPos pos);
	
	public static void buildCube(EntityPlayer player, Blocker blockBuilder, FstPerspective perspective) {
		EnumFacing facing = player.getAdjustedHorizontalFacing();
		BlockPos playerPos = player.getPosition();
		
		for(int h = 0; h < perspective.layers; h++) {
			buildRectangle(
				facing, 
				playerPos.add(0, h, 0), 
				blockBuilder, 
				perspective.rows, 
				perspective.columns
			);
		}
	}	
	
	public static void buildColumn(EnumFacing facing, BlockPos pos, Blocker blockBuilder, int length) {
		for(int i = 0; i < length; i++) {
			pos = Position.forward(facing, pos, 1);
			blockBuilder.setBlock(pos);
		}		
	}
	
	public static void buildRectangle(EnumFacing facing, BlockPos pos, Blocker blockBuilder, int rows, int columns) {
		for(int column = 0; column < columns; column++) {
			buildColumn(
				facing, 
				Position.right(facing, pos, column),
				blockBuilder, rows
			);
		}		
	}
}
