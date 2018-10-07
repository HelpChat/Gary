package me.piggypiglet.gary.core.objects;

import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class FormatScanner {
    private final String str;
    private List<String> keys;
    @Getter private Map<String, String> values;

    /**
     * Populates the message and keys variables, then processes the message.
     * @param str The string that will be scanned.
     * @param keys The keys that str will be scanned for.
     */
    public FormatScanner(String str, String... keys) {
        this.str = str;
        this.keys = new ArrayList<>();
        values = new HashMap<>();
        Arrays.stream(keys).forEach(key -> this.keys.add(key.replace("[", "").replace("]", "").toLowerCase()));
        // Not removing [] from when an array is converted to string, this is incase the user purposely puts [].
        split();
    }

    private void split() {
        Pattern p = Pattern.compile("([\\[][\\w]+[]])(.+)");
        Matcher m = p.matcher(str);

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
}
