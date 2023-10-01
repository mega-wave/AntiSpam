package megawave.antispam;

import megawave.antispam.command.Command;
import megawave.antispam.command.HelpCommand;
import megawave.antispam.command.ReloadCommand;
import megawave.antispam.command.SwitchCommand;
import megawave.antispam.configuration.Configuration;
import megawave.antispam.configuration.ConfigurationImp;
import megawave.antispam.player.APlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.createFile(path);

            InputStream is = getClass().getResourceAsStream("/config.yml");
            OutputStream os = Files.newOutputStream(path);

            Utility.write(is, os);
        }

        return new ConfigurationImp(path);

    }

    public void cellCommand() {
        cmd.add(new ReloadCommand());
        cmd.add(new SwitchCommand());
        cmd.add(new HelpCommand(cmd));

    }

    public boolean callCommand(APlayer player, String[] args) {
        for (int i=0;i<cmd.size();i++) {
            Command c = cmd.get(i);

            if (args.length == 0) {
                return false;
            }

            if (args[0].equals(c.getName())) {
                return c.execute(player, Utility.remove(args, 0));

            }

        }
        return false;

    }

    public Configuration setLang(String loc) throws IOException {
        String lang;
        if (loc.equals("auto")) {
            lang = Locale.getDefault().getLanguage();

            if (!lang.equals("ja")
                    && !lang.equals("en")) {
                lang = "en";
            }
        } else if (loc.equals("ja")
                || loc.equals("en")) {
            lang = loc;
        }else {
            lang = "en";

        }

        Path path = Paths.get("./plugins/AntiSpam/lang/"+lang+".conf");

        if (Files.notExists(path)) {
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.createFile(path);

            InputStream is = getClass().getResourceAsStream("/lang/"+lang+".conf");
            OutputStream os = Files.newOutputStream(path);

            Utility.write(is, os);
        }

        return new ConfigurationImp(path);

    }

    public static Configuration getLang() {
        if (server_type == Antispam.BUKKIT) {
            return BukkitAntispam.lang;

        }else if (server_type == Antispam.BUNGEE) {
            return BungeeAntispam.lang;

        }
        return null;

    }

    public static void reload() {
        if (server_type == Antispam.BUKKIT) {
            BukkitAntispam.reload_c();

        }else if (server_type == Antispam.BUNGEE) {
            BungeeAntispam.reload_c();

        }

    }

    public static int getServer_type() {
        return server_type;
    }
}
