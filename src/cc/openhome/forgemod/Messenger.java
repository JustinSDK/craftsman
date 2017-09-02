package cc.openhome.forgemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class Messenger {
	public static void sendMessageTo(EntityPlayer player, String message) {
		player.sendMessage(new TextComponentString(message));		
	}
}
