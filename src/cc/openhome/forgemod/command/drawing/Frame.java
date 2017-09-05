package cc.openhome.forgemod.command.drawing;

import static cc.openhome.forgemod.FstPerspective.Vertical.UP;

import cc.openhome.forgemod.Blocker;
import cc.openhome.forgemod.Commons;
import cc.openhome.forgemod.FstPerspective;
import cc.openhome.forgemod.Position;
import cc.openhome.forgemod.command.DefaultCommand;
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
        return String.format("/%s <ht|vt> <ux1> <uy1> <uz1> <rows> <columns> <thickness>", getName());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 7) {
            Commons.sendMessageTo(((EntityPlayer) sender), getUsage(sender));
            return;
        }
        
        if("ht".equals(args[0])) {
            horizontalFrame(server, sender, args);
        } else {
            verticalFrame(server, sender, args);
        }
    }

    private void horizontalFrame(MinecraftServer server, ICommandSender sender, String[] args) {
        Cube cube = new Cube();
        
       cube.doCommand(sender, new String[] {args[1], args[2], args[3], args[4], args[6], "1"});
       cube.doCommand(sender, new String[] {args[1], args[2], args[3], args[6], args[5], "1"});
       
       int uz = Integer.parseInt(args[3]);
       int columns = Integer.parseInt(args[5]);
       int thickness = Integer.parseInt(args[6]);

       cube.doCommand(
           sender, 
           new String[] {args[1], args[2], String.valueOf(uz + columns - thickness), args[4], args[6], "1"}
       );
       
       int ux = Integer.parseInt(args[1]);
       int rows = Integer.parseInt(args[4]);
       
       cube.doCommand(
           sender, 
           new String[] {String.valueOf(ux + rows - thickness), args[2], args[3], args[6], args[5], "1"}
       );  
    }
    
    private void verticalFrame(MinecraftServer server, ICommandSender sender, String[] args) {
        Cube cube = new Cube();
        
        cube.doCommand(sender, new String[] {args[1], args[2], args[3], "1", args[6], args[4]});
        cube.doCommand(sender, new String[] {args[1], args[2], args[3], "1", args[5], args[6]});
        
        
        int uz = Integer.parseInt(args[3]);
        int columns = Integer.parseInt(args[5]);
        int thickness = Integer.parseInt(args[6]);

        cube.doCommand(
            sender, 
            new String[] {args[1], args[2], String.valueOf(uz + columns - thickness), "1", args[6], args[4]}
        );
        
        int uy = Integer.parseInt(args[2]);
        int rows = Integer.parseInt(args[4]);
        
        cube.doCommand(
            sender, 
            new String[] {args[1], String.valueOf(uy + rows - thickness), args[3], "1", args[5], args[6]}
        );          
    }  
}