package me.piggypiglet.gary.file;

import com.google.inject.Singleton;
import me.piggypiglet.gary.GaryBootstrap;
import me.piggypiglet.gary.file.framework.AbstractFileConfiguration;
import me.piggypiglet.gary.file.framework.FileConfiguration;
import me.piggypiglet.gary.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class FileManager {
    private final Map<String, FileConfiguration> configs = new HashMap<>();

    public FileConfiguration load(String name, String internalPath, String externalPath) throws Exception {
        LoggerFactory.getLogger("FileManager").info("Loading {}.", name);

        if (externalPath == null) {
            return load(name, internalPath);
        }

        File file = new File(externalPath);

        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();

            if (file.createNewFile() && FileUtils.exportResource(GaryBootstrap.class.getResourceAsStream(internalPath), externalPath)) {
                return load(name, file);
            }
        }

        return load(name, file);
    }

    public FileConfiguration getConfig(String name) {
        return configs.get(name);
    }

    // only works with abstract file configuration extensions
    public void update(String name) {
        FileConfiguration config = getConfig(name);

        if (config instanceof AbstractFileConfiguration) {
            AbstractFileConfiguration ac = (AbstractFileConfiguration) config;
            ac.load(ac.getFile(), FileUtils.readFileToString(ac.getFile()));
        }
    }

    private FileConfiguration load(String name, File file) throws Exception {
        FileConfiguration config = FileConfigurationFactory.get(file);
        configs.put(name, config);
        return config;
    }

    private FileConfiguration load(String name, String path) throws Exception {
        InputStream stream = GaryBootstrap.class.getResourceAsStream(path);
        FileConfiguration config = FileConfigurationFactory.get(path, IOUtils.toString(stream, "UTF-8"));
        stream.close();
        configs.put(name, config);
        return config;
    }
}
