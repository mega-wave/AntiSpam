package megawave.antispam;

import megawave.antispam.command.Command;
import megawave.antispam.configuration.Configuration;
import megawave.antispam.configuration.ConfigurationImp;
import megawave.antispam.player.APlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class Antispam {

    public static Map<UUID, Wrap<List<String>, Long, Long>> playerInfo = new HashMap<>();

    public static List<Command> cmd = new ArrayList<>();

    private static int server_type;
    public final static int BUKKIT = 0;
    public final static int BUNGEE = 1;

    public Antispam(int serverType) { server_type = serverType; }

    public Configuration config_load(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);

            InputStream is = getClass().getResourceAsStream("/config.yml");
            OutputStream os = Files.newOutputStream(path);

            Utility.write(is, os);
        }

        return new ConfigurationImp(path);

    }

    public void CellCommand() {
        //TODO: COMMAND
        //cmd.add();

    }

    public boolean callCommand(APlayer player, String[] args) {
        for (int i=0;i<cmd.size()-1;i++) {
            Command c = cmd.get(i);
            if (args[1].equals(c.getName())) {
                return c.execute(player, args);

            }

        }
        return false;

    }

    public static int getServer_type() {
        return server_type;
    }
}
