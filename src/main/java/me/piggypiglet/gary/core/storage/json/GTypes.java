package me.piggypiglet.gary.core.storage.json;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GTypes {
    @Inject private GFile gFile;

    public String getString(String file, String item) {
        Object item_ = gFile.getItem(file, item);

        if (item_ instanceof String) {
            return (String) item_;
        }

        return "null";
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList(String file, String item) {
        Object item_ = gFile.getItem(file, item);

        if (item_ instanceof List<?>) {
            for (Object object : (List<?>) item_) {
                if (object instanceof String) {
                    return (List<String>) item_;
                }
            }
        }

        return new ArrayList<>();
    }
}
