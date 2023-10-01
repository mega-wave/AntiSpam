package megawave.antispam.command;

import megawave.antispam.Antispam;
import megawave.antispam.player.APlayer;

import java.util.List;

public class HelpCommand implements Command {
    List<Command> cmds;

    public HelpCommand(List<Command> cmds) {
        this.cmds = cmds;

    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return Antispam.getLang().getString("helpcommand_help");
    }

    @Override
    public boolean execute(APlayer player, String[] args) {
        if (args.length == 1) {
            for (Command cmd:cmds) {
                if (cmd.getName().equals(args[0])) {
                    player.sendMessage(cmd.getHelp());
                    return true;

                }

            }
        }else {
            for (Command cmd:cmds) {
                player.sendMessage(cmd.getHelp());

            }
            return true;

        }
        return false;

    }
}
