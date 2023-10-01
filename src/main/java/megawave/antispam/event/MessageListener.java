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

    private static boolean stopper = true;

    private final long rateLimitInterval;
    private final long additionalPeriod;
    private final double similarity_level;
    private final int logs_message;
    private final List<String> ignore;

    public MessageListener(long rateLimitInterval, long additionalPeriod, double similarity_level, int logs_message, List<String> ignore) {
        this.rateLimitInterval = rateLimitInterval;
        this.additionalPeriod = additionalPeriod;
        this.similarity_level = similarity_level;
        this.logs_message = logs_message;
        this.ignore = ignore;

    }
    public void check(APlayer player, String message) {
        if (!ignore.contains(player.getUniqueId().toString()) && stopper) {
            UUID playerId = player.getUniqueId();

            Map<UUID, Wrap<List<String>, Long, Long>> map = Antispam.playerInfo;
            long currentTime = System.currentTimeMillis();
            if (map.get(playerId) == null) {
                map.put(playerId, new Wrap<>(new ArrayList<>(), 0L, 0L));
            }

            Wrap<List<String>, Long, Long> wrap = map.get(playerId);

            List<String> lastMessages = wrap.getX();
            Long playerLastMessageTime = wrap.getY();
            Long playerMessageBlockEndTime = wrap.getZ();

            if (playerLastMessageTime != 0L
                    && currentTime - playerLastMessageTime < rateLimitInterval) {
                player.sendMessage(Antispam.getLang().getString("fastmessage"));
                setCancelled(true);
                return;

            }

            boolean similarMessages = false;
            for (String str : lastMessages) {
                double similarity = Utility.calculateSimilarity(str, message);
                if (100.0D - similarity < similarity_level) {
                    similarMessages = true;
                    break;
                }
            }

            if (lastMessages.size() >= logs_message) {
                lastMessages.remove(0);
            }

            if (similarMessages) {
                player.sendMessage(Antispam.getLang().getString("samemessage"));
                setCancelled(true);
                map.put(playerId, new Wrap<>(lastMessages, currentTime, currentTime + additionalPeriod));
                return;

            }

            lastMessages.add(message);
            map.put(playerId, new Wrap<>(lastMessages, currentTime, playerMessageBlockEndTime));
            setCancelled(false);
        }
    }

    public static void setStopper(boolean stopper) {
        MessageListener.stopper = stopper;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
