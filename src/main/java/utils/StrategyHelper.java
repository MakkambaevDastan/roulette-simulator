package utils;

import constants.Configurations;
import strategy.BaseStrategy;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.reflect.ClassPath;

public class StrategyHelper {

    private static final Logger LOGGER = Logger.getLogger(StrategyHelper.class.getName());
    private static final String STRATEGY_PACKAGE = "strategy";

    public static void saveEnableStrategyClassSet(Set<Class<? extends BaseStrategy>> strategyClassSet)
            throws IOException {
        Objects.requireNonNull(strategyClassSet, "Strategy class set must not be null");
        Path destPath = getSettingFilePath();
        try {
            Files.write(destPath,
                    strategyClassSet.stream()
                            .map(Class::getName)
                            .collect(Collectors.toList()),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            LOGGER.info("Saved " + strategyClassSet.size() + " strategy classes to " + destPath);
        } catch (IOException e) {
            LOGGER.severe("Failed to save strategy class set to " + destPath + ": " + e.getMessage());
            throw e;
        }
    }


    public static Set<Class<? extends BaseStrategy>> getEnableStrategyClassSet() throws IOException {
        Set<Class<? extends BaseStrategy>> strategyClassSet = new HashSet<>();
        Path srcPath = getSettingFilePath();

        if (!Files.exists(srcPath) || !Files.isReadable(srcPath)) {
            LOGGER.fine("Strategy file does not exist or is not readable: " + srcPath);
            return strategyClassSet;
        }

        try {
            for (String line : Files.readAllLines(srcPath)) {
                try {
                    Class<?> clazz = Class.forName(line.trim());
                    if (BaseStrategy.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())
                            && !clazz.isInterface()) {
                        strategyClassSet.add((Class<? extends BaseStrategy>) clazz);
                    } else {
                        LOGGER.warning("Invalid strategy class: " + line);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.warning("Class not found: " + line + " - " + e.getMessage());
                }
            }
            LOGGER.info("Loaded " + strategyClassSet.size() + " strategy classes from " + srcPath);
        } catch (IOException e) {
            LOGGER.severe("Failed to read strategy class set from " + srcPath + ": " + e.getMessage());
            throw e;
        }
        return strategyClassSet;
    }

    public static Set<Class<? extends BaseStrategy>> getAllStrategyClassSet() throws IOException {
        Set<Class<? extends BaseStrategy>> strategyClassSet = new HashSet<>();
        try {
            ClassLoader loader = StrategyHelper.class.getClassLoader();
            ClassPath.from(loader)
                    .getTopLevelClassesRecursive(STRATEGY_PACKAGE)
                    .stream()
                    .map(ClassPath.ClassInfo::load)
                    .filter(clazz -> BaseStrategy.class.isAssignableFrom(clazz)
                            && !Modifier.isAbstract(clazz.getModifiers())
                            && !clazz.isInterface())
                    .map(clazz -> (Class<? extends BaseStrategy>) clazz)
                    .forEach(strategyClassSet::add);
            LOGGER.info("Found " + strategyClassSet.size() + " strategy classes in package " + STRATEGY_PACKAGE);
        } catch (IOException e) {
            LOGGER.severe("Failed to scan strategy classes: " + e.getMessage());
            throw e;
        }
        return strategyClassSet;
    }

    private static Path getSettingFilePath() {
        Objects.requireNonNull(Configurations.ENABLE_STRATEGY_TXT, "Strategy file name must not be null");
        Path path = FileHelper.getSettingFile(Configurations.ENABLE_STRATEGY_TXT).toPath();
        Objects.requireNonNull(path, "Setting file path must not be null");
        return path;
    }
}