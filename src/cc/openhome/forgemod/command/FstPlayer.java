package cc.openhome.forgemod.command;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class FstPlayer {
    private EntityPlayer player;

    public FstPlayer(ICommandSender sender) {
        this.player = (EntityPlayer) sender;
    }
    
    public void info(String message) {
        player.sendMessage(new TextComponentString(message));
    }
    
    public void runIfAirOrBlockHeld(Runnable airOrBlockHeld) {
        runIfAirOrBlockHeld(airOrBlockHeld, airOrBlockHeld); 
    }

    public void runIfAirOrBlockHeld(Runnable blockHeld, Runnable airHeld) {
        Item heldItem = player.getHeldItemMainhand().getItem();
        
        if(heldItem.equals(Items.AIR)) {
            info("You don't hold a block. The command will do the cleaning.");
            airHeld.run();
        } else if(!(heldItem instanceof ItemBlock)) {
            info("Hold a block");
        } else {
            blockHeld.run();
        }
    }    
    
    public void runIfAirOrStairsHeld(Runnable runnable) {
        Item heldItem = player.getHeldItemMainhand().getItem();
        Block heldBlock = Block.getBlockFromItem(heldItem);
        
        if(heldItem.equals(Items.AIR)) {
            info("You don't hold stairs. The command will do the cleaning.");
        } else if(!(heldBlock instanceof BlockStairs)) {
            info("Hold stairs");
            return;
        }        
        
        runnable.run();
    }    
    
    public BlockPos origin(int ux, int uy, int uz) {
        return new FstWalker(player.getAdjustedHorizontalFacing(), player.getPosition())
                        .forward(1 + ux) 
                        .up(uy)           
                        .right(uz)      
                        .getBlockPos();
    }   

    public BlockPos toBlockPos(FstPos pos) {
        FstWalker walker = new FstWalker(player.getAdjustedHorizontalFacing(), player.getPosition());
        return walker.forward(pos.ux).up(pos.uy).right(pos.uz).getBlockPos();
    }
     
    public void buildHeldBlock(FstPos fstPos) {
        buildHeldBlock(toBlockPos(fstPos));
    }
    
    public void buildHeldBlock(BlockPos pos) {
        Item heldItem = player.getHeldItemMainhand().getItem();
        player.getEntityWorld()
            .setBlockState(pos, Block.getBlockFromItem(heldItem).getDefaultState());  
    }    
}
