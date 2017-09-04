package drawing;

import static cc.openhome.forgemod.FstPerspective.Vertical.UP;

import cc.openhome.forgemod.Commons;
import cc.openhome.forgemod.FstPerspective;
import cc.openhome.forgemod.command.DefaultCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

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

        // Player is always regarded as facing to EAST from 1st person perspective. 
        
        FstPerspective perspective = new FstPerspective(
                UP, 
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]), 
                Integer.parseInt(args[5])
            );
        
    }
}
