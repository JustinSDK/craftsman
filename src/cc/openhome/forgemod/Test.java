package cc.openhome.forgemod;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
;

public class Test {
    @SubscribeEvent
    public void test(PlayerTickEvent event) {
    	//System.out.println(event.player.getPosition());
    } 
}
