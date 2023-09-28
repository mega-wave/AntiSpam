package megawave.antispam.player;

import java.util.UUID;

public class BukkitAPlayerImp extends BukkitAPlayer {

    public BukkitAPlayerImp(UUID uuid) {
        super(uuid);

    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);

    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void kick(String reason) {
        player.kickPlayer(reason);
    }

    public static BukkitAPlayer getBukkitAPlayer(UUID uuid) {
        return new BukkitAPlayerImp(uuid);
    }

}
