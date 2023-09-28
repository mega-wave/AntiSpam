package megawave.antispam.player;

import megawave.antispam.Antispam;

import java.util.UUID;

public interface AP {
    void sendMessage(String message);

    static APlayer transfer(UUID uuid) {
        if (Antispam.getServer_type() == Antispam.BUKKIT) {
            return BukkitAPlayerImp.getBukkitAPlayer(uuid);
        } else if (Antispam.getServer_type() == Antispam.BUNGEE) {
            return BungeeAPlayerImp.getBungeeAPlayer(uuid);
        }else {
            //TODO CONSOLE

        }
        return null;
    }

}
