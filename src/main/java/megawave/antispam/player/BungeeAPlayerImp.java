package megawave.antispam.player;

import java.util.UUID;

public class BungeeAPlayerImp extends BungeeAPlayer {
    public BungeeAPlayerImp(UUID uuid) {
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
        player.disconnect(reason);

    }

    public static BungeeAPlayer getBungeeAPlayer(UUID uuid) {
        return new BungeeAPlayerImp((uuid));
    }

}
