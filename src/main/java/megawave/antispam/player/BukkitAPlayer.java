package megawave.antispam.player;

import megawave.antispam.BukkitAntispam;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class BukkitAPlayer extends APlayer {

    public final Player player;

    public BukkitAPlayer(UUID uuid) {
        super(uuid);
        player = BukkitAntispam.getInstance().getServer().getPlayer(uuid);
    }

}
