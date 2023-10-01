package megawave.antispam;

import megawave.antispam.configuration.Configuration;
import megawave.antispam.event.BukkitForwardListener;
import megawave.antispam.player.AP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Paths;

public final class BukkitAntispam extends JavaPlugin {

    public static JavaPlugin instance;

    private Antispam ap;
    private static BukkitForwardListener listener;
    public static Configuration conf;
    public static Configuration lang;

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().info("AntiSpam v1.3 by suzumiya-3, Alefu");
        getLogger().info("Loading plugin..");
        ap = new Antispam(Antispam.BUKKIT);
        conf = null;
        try {
            conf = ap.config_load(Paths.get("./plugins/AntiSpam/plugin.yml"));
        } catch (IOException e) {
            getLogger().severe(e.getMessage());
        }

        listener = new BukkitForwardListener(conf.getInt("rateLimitInterval").longValue()
                , conf.getInt("additionalPeriod").longValue()
                , conf.getDouble("similarity_level")
                , conf.getInt("logs_message")
                , conf.getListString("ignore_player")
        );

        getServer().getPluginManager().registerEvents(
                listener
                , this
        );

        ap.cellCommand();

        try {
            lang = ap.setLang(conf.getString("lang"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getLogger().info(lang.getString("init"));
        instance = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                return ap.callCommand(AP.transfer(p.getUniqueId()), args);
            }
        }
        return false;

    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static void reload_c() {
        AsyncPlayerChatEvent.getHandlerList().unregister(listener);

        listener = new BukkitForwardListener(conf.getInt("rateLimitInterval").longValue()
                , conf.getInt("additionalPeriod").longValue()
                , conf.getDouble("similarity_level")
                , conf.getInt("logs_message")
                , conf.getListString("ignore_player")
        );

        getInstance().getServer().getPluginManager().registerEvents(
                listener
                , getInstance()
        );
    }
}
