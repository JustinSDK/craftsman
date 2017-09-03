package cc.openhome.forgemod;

import java.util.function.Consumer;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class Commons {
    public static void sendMessageTo(EntityPlayer player, String message) {
        player.sendMessage(new TextComponentString(message));
    }
    
    public static void runIfBlockHeld(ICommandSender sender, Runnable runnable) {
        EntityPlayer player = (EntityPlayer) sender;
        
        Item heldItem = player.getHeldItemMainhand().getItem();
        if (heldItem.equals(Items.AIR) || !(heldItem instanceof ItemBlock)) {
            Commons.sendMessageTo(player, "Hold a block");
            return;
        }        
        
        runnable.run();
    }
}
