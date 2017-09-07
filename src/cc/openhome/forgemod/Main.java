package cc.openhome.forgemod;

import cc.openhome.forgemod.command.basic.Build;
import cc.openhome.forgemod.command.basic.Destroy;
import cc.openhome.forgemod.command.basic.Empty;
import cc.openhome.forgemod.command.building.Pyramid;
import cc.openhome.forgemod.command.building.Stairs;
import cc.openhome.forgemod.command.drawing.Cube;
import cc.openhome.forgemod.command.drawing.Frame;
import cc.openhome.forgemod.command.drawing.Turtle;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
        event.registerServerCommand(new Empty());
        event.registerServerCommand(new Destroy());
        event.registerServerCommand(new Build());

        event.registerServerCommand(new Pyramid());
        event.registerServerCommand(new Stairs());

        event.registerServerCommand(new Turtle());
        
        event.registerServerCommand(new Cube());
        event.registerServerCommand(new Frame());
    }
}
