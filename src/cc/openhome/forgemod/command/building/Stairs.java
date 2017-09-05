package cc.openhome.forgemod.command.building;

import cc.openhome.forgemod.command.DefaultCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class Stairs implements DefaultCommand {

    @Override
    public String getName() {
        return "stairs";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <up|down> <ux> <uy> <uz> <height>", getName());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

    }
}
