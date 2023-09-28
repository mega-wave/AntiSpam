package megawave.antispam.event;

import megawave.antispam.player.AP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BukkitForwardListener extends MessageListener implements Listener {

    public BukkitForwardListener(long rateLimitInterval, long additionalPeriod, double similarity_level) {
        super(rateLimitInterval, additionalPeriod, similarity_level);
    }

    @EventHandler
    public void MessageEvent(AsyncPlayerChatEvent e) {
        check(AP.transfer(e.getPlayer().getUniqueId()), e.getMessage());
        e.setCancelled(cancelled);

    }

}