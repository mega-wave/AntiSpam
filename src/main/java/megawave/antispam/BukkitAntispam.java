package megawave.antispam;

import megawave.antispam.configuration.Configuration;
import megawave.antispam.event.BukkitForwardListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Paths;

public class BukkitAntispam extends JavaPlugin {

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().info("AntiSpam v1.0 by liboot");
        getLogger().info("プラグインを初期化中..");
        Antispam ap = new Antispam(Antispam.BUKKIT);
        Configuration conf = null;
        try {
            conf = ap.config_load(Paths.get("./plugins/AntiSpam/plugin.yml"));
        } catch (IOException e) {
            getLogger().severe(e.getMessage());
        }

        getServer().getPluginManager().registerEvents(
                new BukkitForwardListener(conf.getInt("rateLimitInterval").longValue()
                        , conf.getInt("additionalPeriod").longValue()
                        , conf.getDouble("similarity_level")
                        , conf.getInt("logs_message"))
                        , this
        );
        getLogger().info("初期化完了。");

        instance = this;
    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
