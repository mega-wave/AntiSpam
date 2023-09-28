package megawave.antispam.player;

import java.util.UUID;

public abstract class APlayer implements AP {

    public UUID uuid;

    public APlayer(UUID uuid) {
        this.uuid = uuid;

    }

    public abstract String getName();

    public UUID getUniqueId() {
        return uuid;

    }

    public void kick() {
        kick("");

    }

    public abstract void kick(String reason);

}
