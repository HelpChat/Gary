package me.piggypiglet.gary.core.utils.file.modules;

import java.io.File;
import java.io.FilenameFilter;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        int i = name.lastIndexOf('.');
        return i > 0 && name.substring(i).equals(".jar");
    }
}
