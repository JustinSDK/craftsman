package cc.openhome.forgemod.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface DefaultCommand extends ICommand {
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
