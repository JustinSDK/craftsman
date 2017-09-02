package cc.openhome.forgemod;

import cc.openhome.forgemod.command.BuildCommand;

import cc.openhome.forgemod.command.DestroyCommand;
import cc.openhome.forgemod.command.EmptyCommand;
import cc.openhome.forgemod.command.PyramidCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
	public static final String MODID = "craftsman";
	public static final String VERSION = "0.1";
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void init(FMLServerStartingEvent event) {
		event.registerServerCommand(new EmptyCommand());
		event.registerServerCommand(new DestroyCommand());
		event.registerServerCommand(new BuildCommand());
		
		event.registerServerCommand(new PyramidCommand());
	}
}
