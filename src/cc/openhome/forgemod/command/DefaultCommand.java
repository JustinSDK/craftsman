package cc.openhome.forgemod.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface DefaultCommand extends ICommand {
    int lengthOfArgs();
    
    @Override
    default void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {     
        if (args.length != lengthOfArgs()) {
            Commons.sendMessageTo(((EntityPlayer) sender), getUsage(sender));
        } 
        else {
            doCommand(server, sender, args);
        }
    }
    
    void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
    
    @Override
    default int compareTo(ICommand o) {
        return 0;
    }
    
    @Override
    default List<String> getAliases() {
        return Arrays.asList(getName());
    }
    
    @Override
    default boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    @Override
    default List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
            BlockPos targetPos) {
        return null;
    }

    @Override
    default boolean isUsernameIndex(String[] args, int index) {
        return false;
    }    
}
