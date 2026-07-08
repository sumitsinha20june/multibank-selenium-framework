package com.multibank.framework.utilities;

import com.multibank.framework.exceptions.FrameworkException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

// Filesystem helpers: create/clean/resolve directories and read/copy files.
// Wraps java.nio and Apache Commons IO with framework exceptions.
public final class FileUtil {
    private FileUtil() {
    }

    public static void createDirectories(Path dir) {
        if (dir == null) return;
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new FrameworkException("Failed to create directory: " + dir, e);
        }
    }

    public static void copyFile(File source, Path target) {
        try {
            FileUtils.copyFile(source, target.toFile());
        } catch (IOException e) {
            throw new FrameworkException("Failed to copy file from " + source + " to " + target, e);
        }
    }

    public static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new FrameworkException("Failed to read file: " + path, e);
        }
    }

    public static FileInputStream openInputStream(Path path) {
        try {
            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new FrameworkException("Failed to open file: " + path, e);
        }
    }

    public static void cleanDirectory(Path dir) {
        if (dir == null || !Files.exists(dir)) {
            return;
        }
        try {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .filter(p -> !p.equals(dir))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            throw new FrameworkException("Failed to clean directory: " + dir, e);
        }
    }
}
