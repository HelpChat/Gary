package me.piggypiglet.gary.core.objects;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class FormatScanner {
    private final Message message;
    private List<String> keys;
    @Getter private Map<String, String> values;

    /**
     * Populates the message and keys variables, then processes the message.
     * @param message The message that will be scanned.
     * @param keys The keys that str will be scanned for.
     */
    public FormatScanner(Message message, String... keys) {
        this.message = message;
        this.keys = new ArrayList<>();
        values = new HashMap<>();
        Arrays.stream(keys).forEach(key -> this.keys.add(key.replace("[", "").replace("]", "").toLowerCase()));
        // Not removing [] from when an array is converted to string, this is incase the user purposely puts [].
        split();
    }

    private void split() {
        Pattern p = Pattern.compile("([\\[][\\w]+[]])(.+)");
        Matcher m = p.matcher(message.getContentRaw());

        while (m.find()) {
            String key = m.group(1).replace("[", "").replace("]", "").toLowerCase();
            String value = m.group(2);

            if (keys.contains(key)) {
                values.put(key.trim(), value.trim());
            }
        }
    }

    /**
     *
     * @return Returns a boolean specifying whether the map contains the same keys that the user specified in the constructor.
     */
    public boolean containsKeys() {
        return values.keySet().containsAll(keys);
    }

    /**
     * Generate a MessageEmbed instance from the contents in this class.
     * @param titleKey The value to use for the title
     * @return Returns a MessageEmbed instance containing information stored in this class.
     */
    public MessageEmbed toEmbed(String titleKey) {
        titleKey = titleKey.toLowerCase();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, values.getOrDefault(titleKey, "null")), null, message.getAuthor().getEffectiveAvatarUrl())
                .setColor(Constants.GARY_COLOR)
                .setTimestamp(ZonedDateTime.now());

        Map<String, String> tempMap = values;
        tempMap.remove(titleKey);
        tempMap.forEach((key, value) -> embedBuilder.addField(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, key) + ":", value, false));

        return embedBuilder.build();
    }
}
