package megawave.antispam.command;

import megawave.antispam.player.APlayer;

public interface Command {

    String getName();

    String getHelp();

    boolean execute(APlayer player, String[] args);

}
