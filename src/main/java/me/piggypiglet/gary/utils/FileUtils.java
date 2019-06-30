package me.piggypiglet.gary.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class FileUtils {
    public static boolean exportResource(InputStream source, String destination) {
        boolean success = true;

        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    public static String readFileToString(File file) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
