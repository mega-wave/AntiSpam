package megawave.antispam.command;

import megawave.antispam.player.APlayer;

public interface Command {

    String getName();

    boolean execute(APlayer player, String[] args);

}
