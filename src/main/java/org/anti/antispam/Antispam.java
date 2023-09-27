package org.anti.antispam;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Antispam extends JavaPlugin implements Listener {
    private final Map<UUID, List<String>> playerMessages = new HashMap<>();
    private final Map<UUID, Long> playerMessageBlockEndTime = new HashMap<>();
    private final Map<UUID, Long> playerLastMessageTime = new HashMap<>();
    private final long rateLimitInterval = 500L;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        UUID playerId = player.getUniqueId();

        List<String> lastMessages = playerMessages.getOrDefault(playerId, new ArrayList<>());

        if (playerMessageBlockEndTime.getOrDefault(playerId, 0L) > System.currentTimeMillis()) {
            player.sendMessage(ChatColor.RED + "チャットがブロックされています。");
            getLogger().info(playerId+" チャットがブロックされています。");
            e.setCancelled(true);
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (playerLastMessageTime.containsKey(playerId) &&
                currentTime - playerLastMessageTime.get(playerId) < rateLimitInterval) {
            player.sendMessage(ChatColor.RED + "メッセージを送信する速度が速すぎます。");
            getLogger().info(playerId+" メッセージを送信する速度が速すぎます。");
            e.setCancelled(true);
            return;
        }

        boolean similarMessages = false;
        for (String message : lastMessages) {
            double similarity = calculateSimilarity(message, e.getMessage());
            if (similarity > 85.0) {
                similarMessages = true;
                break;
            }
        }

        if (similarMessages) {
            player.sendMessage(ChatColor.RED + "同じメッセージを連続して送信しないでください。2秒間ブロックされます。");
            getLogger().info(playerId+" 同じメッセージを連続して送信しないでください。2秒間ブロックされます。");
            e.setCancelled(true);
            playerMessageBlockEndTime.put(playerId, currentTime + 2000L);
            return;
        }

        if (lastMessages.size() >= 4) {
            lastMessages.remove(0);
        }
        lastMessages.add(e.getMessage());
        playerMessages.put(playerId, lastMessages);

        playerLastMessageTime.put(playerId, currentTime);
    }

    public static double calculateSimilarity(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int lenS1 = s1.length();
        int lenS2 = s2.length();

        if (lenS1 == 0 || lenS2 == 0) {
            return 0.0;
        }

        int[][] matrix = new int[lenS1 + 1][lenS2 + 1];
        for (int i = 0; i <= lenS1; i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j <= lenS2; j++) {
            matrix[0][j] = j;
        }
        for (int i = 1; i <= lenS1; i++) {
            for (int j = 1; j <= lenS2; j++) {
                int cost = (s1.charAt(i - 1) != s2.charAt(j - 1)) ? 1 : 0;
                matrix[i][j] = min(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1, matrix[i - 1][j - 1] + cost);
            }
        }

        double maxLength = Math.max(lenS1, lenS2);
        double distance = matrix[lenS1][lenS2];
        double similarity = (1.0 - distance / maxLength) * 100.0;
        return similarity;
    }

    public static int min(int a, int b, int c) {
        if (a < b && a < c) {
            return a;
        } else if (b < c) {
            return b;
        }
        return c;
    }
}
