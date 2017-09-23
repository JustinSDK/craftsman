package cc.openhome.forgemod.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStairs;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import scala.actors.threadpool.Arrays;

public class Args {
    public static Map<String, Integer> argsToInteger(String[] argNames, String[] args) {
        Map<String, Integer> argsToInteger = new HashMap<>();
        for(int i = 0; i < argNames.length; i++) {
            argsToInteger.put(argNames[i], Integer.parseInt(args[i]));
        }
        return argsToInteger;
    }
    
    public static String[] copyArgs(String[] args, int from) {
        return (String[]) Arrays.copyOfRange(args, from, args.length);
    }
}
