package megawave.antispam;

import megawave.antispam.configuration.Configuration;
import megawave.antispam.event.BungeeForwardListener;
import megawave.antispam.player.AP;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Paths;

public final class BungeeAntispam extends Plugin {
    public static Plugin plugin;
    private static BungeeForwardListener listener;

    public static Configuration conf;
    public static Configuration lang;

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().info("AntiSpam v1.3 by suzumiya-3, Alefu");
        getLogger().info("プラグインを初期化中..");
        Antispam ap = new Antispam(Antispam.BUNGEE);
        conf = null;
        try {
            conf = ap.config_load(Paths.get("./plugins/AntiSpam/plugin.yml"));
        } catch (IOException e) {
            getLogger().severe(e.getMessage());
        }

        listener = new BungeeForwardListener(conf.getInt("rateLimitInterval").longValue()
                , conf.getInt("additionalPeriod").longValue()
                , conf.getDouble("similarity_level")
                , conf.getInt("logs_message")
                , conf.getListString("ignore_player"));

        getProxy().getPluginManager().registerListener(
                this, listener
        );

        ap.cellCommand();

        getProxy().getPluginManager().registerCommand(this, new Command("antispam", "megawave") {
                    @Override
                    public void execute(CommandSender sender, String[] args) {
                        ap.callCommand(AP.transfer(((ProxiedPlayer) sender).getUniqueId()), args);
                    }
                }
        );

        try {
            lang = ap.setLang(conf.getString("lang"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getLogger().info(lang.getString("init"));

        plugin = this;
    }

    public static Plugin getInstance() {
        return plugin;

    }

    public static void reload_c() {
        getInstance().getProxy().getPluginManager().unregisterListener(listener);
        listener = new BungeeForwardListener(conf.getInt("rateLimitInterval").longValue()
                , conf.getInt("additionalPeriod").longValue()
                , conf.getDouble("similarity_level")
                , conf.getInt("logs_message")
                , conf.getListString("ignore_player")
        );
        getInstance().getProxy().getPluginManager().registerListener(getInstance(), listener);

    }
}
