package cc.openhome.forgemod.command.drawing;

import java.util.ArrayList;
import java.util.List;

import cc.openhome.forgemod.command.Commons;
import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstDimension;
import cc.openhome.forgemod.command.FstPos;
import cc.openhome.forgemod.command.Walker;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class Line implements DefaultCommand {

    @Override
    public String getName() {
        return "line";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <ux1> <uy1> <uz1> <ux2> <uy2> <uz2>", getName());
    } 

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;

        if (args.length != 6) {
            player.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }
        
        Commons.runIfAirOrBlockHeld(sender, () -> {
            Item heldItem = player.getHeldItemMainhand().getItem();

            BlockPos start = toBlockPos(new FstPos(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2])
                ), player);
            
            BlockPos end = toBlockPos(new FstPos(
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]),
                    Integer.parseInt(args[5])
                ), player);      
            
            for(BlockPos pos : posList(start, end)) {
                player.getEntityWorld()
                      .setBlockState(pos, Block.getBlockFromItem(heldItem).getDefaultState());
            }
        });
    }
    
    private int ax;
    private int ay;
    private int az;
    
    private int sx;
    private int sy;
    private int sz;       
    
    private List<BlockPos> posList(BlockPos start, BlockPos end) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        int dz = end.getZ() - start.getZ();
        
        ax = Math.abs(dx) << 1;
        ay = Math.abs(dy) << 1;
        az = Math.abs(dz) << 1;
        
        sx = zsgn(dx);
        sy = zsgn(dy);
        sz = zsgn(dz);        
        
        if(ax >= Math.max(ay, az)) {
            return xDominant(start, end);
        } else if(ay >= Math.max(ax, az)) {
            return yDominant(start, end);
        } 
        return zDominant(start, end);
    }

    private List<BlockPos> xDominant(BlockPos start, BlockPos end) {
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        
        int yd = ay - (ax >> 1);
        int zd = az - (ax >> 1);
        
        List<BlockPos> posLt = new ArrayList<>();
        do {
            posLt.add(new BlockPos(x, y, z));
            if(yd >= 0) {
                y += sy;
                yd -= ax;
            }
            if(zd >= 0) {
                z += sz;
                zd -= ax;                    
            }
            x += sx;
            yd += ay;
            zd += az;
        } while(x != end.getX());
        return posLt;
    }

    private List<BlockPos> yDominant(BlockPos start, BlockPos end) {
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        
        int xd = ax - (ay >> 1);
        int zd = az - (ay >> 1);
        
        List<BlockPos> posLt = new ArrayList<>();
        do {
            posLt.add(new BlockPos(x, y, z));
            if(xd >= 0) {
                x += sx;
                xd -= ay;
            }
            if(zd >= 0) {
                z += sz;
                zd -= ay;                    
            }
            y += sy;
            xd += ax;
            zd += az;
        } while(y != end.getY());
        return posLt;
    }
    
    private List<BlockPos> zDominant(BlockPos start, BlockPos end) {
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        
        int xd = ax - (az >> 1);
        int yd = ay - (az >> 1);
        
        List<BlockPos> posLt = new ArrayList<>();
        do {
            posLt.add(new BlockPos(x, y, z));
            if(xd >= 0) {
                x += sx;
                xd -= az;
            }
            if(yd >= 0) {
                y += sy;
                yd -= az;                    
            }
            z += sz;
            xd += ax;
            yd += ay;
        } while(z != end.getZ());
        return posLt;
    }    
    
    private BlockPos toBlockPos(FstPos pos, EntityPlayer player) {
        Walker walker = new Walker(player.getAdjustedHorizontalFacing(), player.getPosition());
        return walker.forward(pos.ux).up(pos.uy).right(pos.uz).getBlockPos();
    }
    
    private int zsgn(int a) {
        if(a < 0) {
            return -1;
        } 
        else if(a > 0) {
            return 1;
        }
        return 0;
    }
}
