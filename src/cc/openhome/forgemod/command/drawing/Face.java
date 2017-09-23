package cc.openhome.forgemod.command.drawing;

import static cc.openhome.forgemod.command.Commons.argsToInteger;

import static cc.openhome.forgemod.command.Commons.buildHeldBlock;
import static cc.openhome.forgemod.command.Commons.copyArgs;
import static cc.openhome.forgemod.command.Commons.runIfAirOrBlockHeld;
import static cc.openhome.forgemod.command.Commons.toBlockPos;
import static cc.openhome.forgemod.command.Commons.sendMessageTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstPlayer;
import cc.openhome.forgemod.command.FstPos;
import cc.openhome.forgemod.command.LinePts;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class Face implements DefaultCommand {

    @Override
    public String getName() {
        return "face";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <begin>%n" , getName()) +
                String.format("/%s <add <ux> <uy> <uz>%n" , getName()) +
                String.format("/%s <end|endFill>" , getName());
    }

    @Override
    public int minLengthOfArgs() {
        return 1;
    }
    
    List<BlockPos> facePosLt;

    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        FstPlayer player = new FstPlayer(sender);
        
        player.runIfAirOrBlockHeld(() -> {
            if("begin".equals(args[0])) {
               facePosLt = new ArrayList<>();
               player.info("Ready, Use 'face add <ux> <uy> <uz>' to add points.");
            } 
            else if("add".equals(args[0])) {
                Map<String, Integer> argsInt = argsToInteger(
                        new String[] {"ux", "uy", "uz"}, 
                        copyArgs(args, 1)
                );
                FstPos facePos = new FstPos(
                        argsInt.get("ux"),
                        argsInt.get("uy"),
                        argsInt.get("uz"));
                facePosLt.add(player.toBlockPos(facePos));
                
                player.buildHeldBlock(facePos);
            } else if("end".equals(args[0])) {
                buildFace(player, false);
            } else if("endFill".equals(args[0])) {
                buildFace(player, true);
            }
        });
    }

    private void buildFace(FstPlayer player, boolean filled) {
        List<BlockPos> edgesVertices = edgesPts();
        
        if(filled) {
            Collections.sort(edgesVertices, (pt1, pt2) -> pt1.getZ() - pt2.getZ());
            Collections.sort(edgesVertices, (pt1, pt2) -> pt1.getY() - pt2.getY());
            Collections.sort(edgesVertices, (pt1, pt2) -> pt1.getX() - pt2.getX());
            
            BlockPos lastPt = edgesVertices.get(0);
            for(int size = edgesVertices.size(), i = 1; i < size; i++) {
                BlockPos pt = edgesVertices.get(i);
                new LinePts(lastPt, pt).getList().forEach(pos -> player.buildHeldBlock(pos));
                lastPt = pt;
            }
        } 
        else {
            edgesVertices.forEach(pos -> player.buildHeldBlock(pos));
        }
    }

    private List<BlockPos> edgesPts() {
        List<BlockPos> edgesPts = new ArrayList<>();
        BlockPos fstPt = facePosLt.get(0);
        BlockPos lastPt = facePosLt.get(0);
        
        for(int size = facePosLt.size(), i = 1; i < size; i++) {
            BlockPos pt = facePosLt.get(i);
            edgesPts.addAll(
                new LinePts(lastPt, pt).getList()
            );
            lastPt = pt;
        }
        edgesPts.addAll(
            new LinePts(lastPt, fstPt).getList()
        );
        return edgesPts;
    }
}
