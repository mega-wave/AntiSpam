package megawave.antispam.player;

import megawave.antispam.BungeeAntispam;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public abstract class BungeeAPlayer extends APlayer {

    public final ProxiedPlayer player;

    protected BungeeAPlayer(UUID uuid) {
        super(uuid);
        player = BungeeAntispam.getInstance().getProxy().getPlayer(uuid);

    }
}
