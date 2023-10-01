package megawave.antispam.command;

import megawave.antispam.Antispam;
import megawave.antispam.player.APlayer;

public class ReloadCommand implements Command {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getHelp() {
        return Antispam.getLang().getString("reloadcommand_help");
    }

    @Override
    public boolean execute(APlayer player, String[] args) {
        Antispam.reload();
        player.sendMessage(Antispam.getLang().getString("reloadcommand_notice"));
        return true;

    }
}
