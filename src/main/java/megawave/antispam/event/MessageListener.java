package megawave.antispam.event;

import megawave.antispam.Antispam;
import megawave.antispam.Utility;
import megawave.antispam.Wrap;
import megawave.antispam.player.APlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class MessageListener implements Event {
    boolean cancelled;

    private final long rateLimitInterval;
    private final long additionalPeriod;
    private final double similarity_level;

    public MessageListener(long rateLimitInterval, long additionalPeriod, double similarity_level) {
        this.rateLimitInterval = rateLimitInterval;
        this.additionalPeriod = additionalPeriod;
        this.similarity_level = similarity_level;

    }

    public void check(APlayer player, String message) {
        UUID playerId = player.getUniqueId();
        String playerName = player.getName();

        Map<UUID, Wrap<List<String>, Long, Long>> map = Antispam.playerInfo;
        if (map.get(playerId) == null) {
            map.put(playerId, new Wrap<>(new ArrayList<>(), 0L, -1L));
        }

        Wrap<List<String>, Long, Long> wrap = map.get(playerId);

        List<String> lastMessages = wrap.getX();
        Long playerLastMessageTime = wrap.getY();
        Long playerMessageBlockEndTime = wrap.getZ();

        long currentTime = System.currentTimeMillis();

        if (playerMessageBlockEndTime > currentTime) {
            player.sendMessage("§4 チャットがブロックされています。");
            this.setCancelled(true);
            return;
        }

        if (playerLastMessageTime != null &&
                currentTime - playerLastMessageTime < rateLimitInterval) {
            player.sendMessage("§4 メッセージを送信する速度が速すぎます。");
            System.out.printf("%s(%s)はメッセージを連投したため%d秒間投稿をブロックします。%n", playerName, playerId, rateLimitInterval / 1000);
            this.setCancelled(true);
            return;
        }

        boolean similarMessages = false;
        for (String str : lastMessages) {
            double similarity = Utility.calculateSimilarity(str, message);
            if (similarity > similarity_level) {
                similarMessages = true;
                break;
            }
        }

        if (similarMessages) {
            player.sendMessage(String.format("§4 同じメッセージを連続して送信しないでください。%d秒間ブロックされます。", additionalPeriod / 1000));
            System.out.printf("%s(%s)はおなじメッセージを投稿したため%d秒間投稿をブロックします。", playerName, playerId, additionalPeriod / 1000);
            this.setCancelled(true);
            map.put(playerId, new Wrap<>(lastMessages, currentTime + additionalPeriod, playerMessageBlockEndTime));
            return;
        }

        if (lastMessages.size() >= 4) {
            lastMessages.remove(0);
        }
        lastMessages.add(message);
        map.put(playerId, new Wrap<>(lastMessages, currentTime, playerMessageBlockEndTime));

    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
