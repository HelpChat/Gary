package me.piggypiglet.gary.core.objects.services;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class FormatScanner {
    private final Message message;
    @Getter private Map<String, String> values;

    /**
     * Populates the message and keys variables, then processes the message.
     * @param message The message that will be scanned.
     */
    public FormatScanner(Message message) {
        this.message = message;
        values = new HashMap<>();
        // Not removing [] from when an array is converted to string, this is incase the user purposely puts [].
        split();
    }

    private void split() {
        Pattern p = Pattern.compile("([\\[][\\w]+[]])(.+)");
        Matcher m = p.matcher(message.getContentRaw());

        while (m.find()) {
            String key = m.group(1).replace("[", "").replace("]", "").toLowerCase();
            String value = m.group(2);

            values.put(key.trim(), value.trim());
        }

        System.out.println(values);
    }

    /**
     * Check if the message supplied contains keys specified
     * @param keys Which keys to scan for
     * @return Returns a boolean specifying whether the map contains the same keys that the user specified in the constructor, or keys specified in this methods parameter
     */
    public boolean containsKeys(String... keys) {
        return values.keySet().containsAll(keys.length >= 1 ? Arrays.asList(keys) : new ArrayList<>());
    }

    /**
     * Generate an EmbedBuilder instance from the contents in this class.
     * @param titleKey The value to use for the title
     * @param keys Specific keys to include in the loop, if not set will use all keys in the class.
     * @return Returns an EmbedBuilder instance containing information stored in this class.
     */
    public EmbedBuilder toEmbed(String titleKey, String... keys) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(values.getOrDefault(titleKey, "null"), null, message.getAuthor().getEffectiveAvatarUrl())
                .setColor(Constants.GARY_COLOR)
                .setTimestamp(ZonedDateTime.now());

        Arrays.stream(keys).forEach(key -> {
            if (values.containsKey(key.toLowerCase())) {
                embedBuilder.addField(key + ":", values.get(key.toLowerCase()), false);
            }
        });

        return embedBuilder;
    }
}
