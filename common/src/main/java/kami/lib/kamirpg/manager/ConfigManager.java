package kami.lib.kamirpg.manager;

import kami.lib.kamirpg.services.ConfigService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
    private final ConcurrentMap<String, ConfigService> configs = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(ConfigManager.class.getName());

    public void addConfig(String name, Path configPath) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Config name cannot be null or empty");
        if (configPath == null) throw new IllegalArgumentException("Config path cannot be null");
        if (configs.containsKey(name)) throw new IllegalArgumentException("Config with the same name already exists");
        configs.put(name, new ConfigService(configPath));
    }

    public Optional<ConfigService> getConfig(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Config name cannot be null or empty");
        return Optional.ofNullable(configs.get(name)).map(config -> {
            try {
                config.loadDefaultSettings();
                return config;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to load config: " + name, e);
                return null;
            }
        });
    }

    public void removeConfig(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Config name cannot be null or empty");
        if (!configs.containsKey(name)) throw new IllegalArgumentException("No config with the given name exists");
        configs.remove(name);
    }

    public boolean configExists(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Config name cannot be null or empty");
        return configs.containsKey(name);
    }

    public void loadAllConfigsFromDirectory(Path directory) {
        if (directory == null) throw new IllegalArgumentException("Directory cannot be null");
        try {
            Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .forEach(file -> addConfig(file.getFileName().toString(), file));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load configs from directory: " + directory, e);
        }
    }
}