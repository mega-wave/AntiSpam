package megawave.antispam.event;

import megawave.antispam.player.AP;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class BungeeForwardListener extends MessageListener implements Listener {

    public BungeeForwardListener(long rateLimitInterval, long additionalPeriod, double similarity_level, int logs_message, List<String> ignore) {
        super(rateLimitInterval, additionalPeriod, similarity_level, logs_message, ignore);
    }

    @EventHandler
    public void MessageEvent(ChatEvent e) {
        if (e.getSender() instanceof ProxiedPlayer) {
            check(AP.transfer(((ProxiedPlayer) e.getSender()).getUniqueId()), e.getMessage());
            e.setCancelled(cancelled);
        }

    }

}
