package megawave.antispam;

import megawave.antispam.configuration.Configuration;
import megawave.antispam.event.BungeeForwardListener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Paths;

public final class BungeeAntispam extends Plugin  {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().info("AntiSpam v1.0 by liboot");
        getLogger().info("プラグインを初期化中..");
        Antispam ap = new Antispam(Antispam.BUNGEE);
        Configuration conf = null;
        try {
            conf = ap.config_load(Paths.get("./plugins/AntiSpam/plugin.yml"));
        } catch (IOException e) {
            getLogger().severe(e.getMessage());
        }

        getProxy().getPluginManager().registerListener(
                this, new BungeeForwardListener(conf.getInt("rateLimitInterval").longValue()
                        , conf.getInt("additionalPeriod").longValue()
                        , conf.getDouble("similarity_level")
                        , conf.getInt("logs_message"))
        );

        plugin = this;
        getLogger().info("初期化完了。");
    }

    public static Plugin getInstance() {
        return plugin;

    }
}
