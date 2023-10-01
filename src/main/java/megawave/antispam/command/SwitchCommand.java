package megawave.antispam.command;

import megawave.antispam.Antispam;
import megawave.antispam.event.MessageListener;
import megawave.antispam.player.APlayer;

public class SwitchCommand implements Command {
    @Override
    public String getName() {
        return "switch";
    }

    @Override
    public String getHelp() {
        return Antispam.getLang().getString("switchcommand_help");
    }

    @Override
    public boolean execute(APlayer player, String[] args) {
        if (args.length == 1) {
            if (args[0].equals("true")) {
                MessageListener.setStopper(true);

            } else if (args[0].equals("false")) {
                MessageListener.setStopper(false);

            }
            player.sendMessage(Antispam.getLang().getString("switchcommand_notice"));

        }

        return false;
    }
}
