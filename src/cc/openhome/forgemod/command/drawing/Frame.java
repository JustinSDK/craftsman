package cc.openhome.forgemod.command.drawing;

import cc.openhome.forgemod.command.Args;
import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstDimension;
import cc.openhome.forgemod.command.FstPlayer;
import cc.openhome.forgemod.command.FstWalker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Frame implements DefaultCommand {

    @Override
    public String getName() {
        return "frame";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <ht|vt> <ux> <uy> <uz> <rows> <columns> <thickness>", getName());
    }

    public int minLengthOfArgs() {
        return 7;
    }
    
    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {     
        FstPlayer player = new FstPlayer(sender);
        player.runIfAirOrBlockHeld(() -> {
            if("ht".equals(args[0])) {
                horizontalFrame(player, args);
            } else {
                verticalFrame(player, args);
            }
        });        
    }

    private void horizontalFrame(FstPlayer player, String[] args) {
        Cube cube = new Cube();
        
        cube.doCommandWithoutCheckingBlock(player, new String[] {args[1], args[2], args[3], args[4], args[6], "1"});
        cube.doCommandWithoutCheckingBlock(player, new String[] {args[1], args[2], args[3], args[6], args[5], "1"});
       
        int uz = Integer.parseInt(args[3]);
        int columns = Integer.parseInt(args[5]);
        int thickness = Integer.parseInt(args[6]);
    
        cube.doCommandWithoutCheckingBlock(
            player, 
            new String[] {args[1], args[2], String.valueOf(uz + columns - thickness), args[4], args[6], "1"}
        );
       
        int ux = Integer.parseInt(args[1]);
        int rows = Integer.parseInt(args[4]);
       
        cube.doCommandWithoutCheckingBlock(
            player, 
            new String[] {String.valueOf(ux + rows - thickness), args[2], args[3], args[6], args[5], "1"}
        );  
    }
    
    private void verticalFrame(FstPlayer player, String[] args) {
        Cube cube = new Cube();
        
        cube.doCommandWithoutCheckingBlock(player, new String[] {args[1], args[2], args[3], "1", args[6], args[4]});
        cube.doCommandWithoutCheckingBlock(player, new String[] {args[1], args[2], args[3], "1", args[5], args[6]});
        
        
        int uz = Integer.parseInt(args[3]);
        int columns = Integer.parseInt(args[5]);
        int thickness = Integer.parseInt(args[6]);

        cube.doCommandWithoutCheckingBlock(
            player, 
            new String[] {args[1], args[2], String.valueOf(uz + columns - thickness), "1", args[6], args[4]}
        );
        
        int uy = Integer.parseInt(args[2]);
        int rows = Integer.parseInt(args[4]);
        
        cube.doCommandWithoutCheckingBlock(
            player, 
            new String[] {args[1], String.valueOf(uy + rows - thickness), args[3], "1", args[5], args[6]}
        );          
    }  
}