package kami.lib.kamirpg.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigService {
    private final Path configPath;
    private Properties config;
    private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(ConfigService.class.getName());

    public ConfigService(Path configPath) {
        if (configPath == null) throw new IllegalArgumentException("Config path cannot be null");
        this.configPath = configPath;
    }

    public void load() throws IOException {
        config = new Properties();
        if (Files.exists(configPath))
            try (FileInputStream in = new FileInputStream(configPath.toFile())) {
            config.load(in);
        }
        cache.clear();
    }

    public void save() throws IOException {
        try (FileOutputStream out = new FileOutputStream(configPath.toFile())) {
            config.store(out, null);
        }
    }

    public void update(String key, String value) {
        config.setProperty(key, value);
        cache.put(key, value);
    }

    public void remove(String key) {
        config.remove(key);
        cache.remove(key);
    }

    public String get(String key) {
        return cache.computeIfAbsent(key, k -> config.getProperty(k));
    }

    public void loadDefaultSettings() throws IOException {
        load();
    }

    public void saveDefaultSettings() throws IOException {
        save();
    }

    public boolean getBool(String key) {
        String value = get(key);
        return Boolean.parseBoolean(value);
    }

    public void reloadSettings() throws IOException {
        load();
    }
    public int getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Failed to parse int value for key: " + key, e);
            return 0;
        }
    }

    public long getLong(String key) {
        String value = get(key);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Failed to parse long value for key: " + key, e);
            return 0L;
        }
    }

    public double getDouble(String key) {
        String value = get(key);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Failed to parse double value for key: " + key, e);
            return 0.0;
        }
    }

    public float getFloat(String key) {
        String value = get(key);
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Failed to parse float value for key: " + key, e);
            return 0.0f;
        }
    }

    public boolean containsKey(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return config.containsKey(key);
    }

    public boolean containsValue(String value) {
        if (value == null) throw new IllegalArgumentException("Value cannot be null");
        return config.containsValue(value);
    }
}