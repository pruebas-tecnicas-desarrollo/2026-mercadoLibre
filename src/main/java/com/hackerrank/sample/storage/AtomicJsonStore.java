package com.hackerrank.sample.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.model.ProductInformation;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
// Thread-safe JSON file store that provides atomic read/write operations.
// Designed for small-scale persistence without a database.
public class AtomicJsonStore {

    private final Path filePath;
    private final ObjectMapper mapper;

    // Read-write lock to allow concurrent reads while serializing writes.
    // Fair mode is enabled to avoid writer starvation.
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public AtomicJsonStore(ObjectMapper mapper) {
        this.filePath = Paths.get("data/products.json");
        this.mapper = mapper;
        ensureFileExists();
    }

    public Map<String, ProductInformation> readAll() {
        lock.readLock().lock();
        try {
            return readAllUnlocked();
        } finally {
            lock.readLock().unlock();
        }
    }

    public <T> T writeAtomically(Function<Map<String, ProductInformation>, T> file) {
        lock.writeLock().lock();
        try {
            Map<String, ProductInformation> data = readAllUnlocked();
            T result = file.apply(data);
            writeAllUnlocked(data);
            return result;
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Reads the JSON content without acquiring locks.
    // Must be called only from methods that already manage locking.
    private Map<String, ProductInformation> readAllUnlocked() {
        try {
            byte[] bytes = Files.readAllBytes(filePath);
            if (bytes.length == 0) {
                return new LinkedHashMap<>();
            }

            Map<String, ProductInformation> raw = mapper.readValue(bytes, Map.class);
            if (raw == null || raw.isEmpty()) {
                return new LinkedHashMap<>();
            }

            return raw.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> mapper.convertValue(entry.getValue(), ProductInformation.class)
                ));

        } catch (NoSuchFileException e) {
            return new LinkedHashMap<>();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read file: " + filePath.toAbsolutePath(), e);
        }
    }

    // Writes the full JSON content without acquiring locks.
    // Must be called only from methods that already manage locking.
    private void writeAllUnlocked(Map<String, ProductInformation> data) {
        try {
            Path tmp = tempSibling(filePath);

            byte[] bytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(data);
            Files.write(tmp, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            try {
                Files.move(
                        tmp,
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(tmp, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not write file: " + filePath.toAbsolutePath(), e);
        }
    }

    // Ensures the storage file and its parent directories exist at startup.
    private void ensureFileExists() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(filePath)) {
                Files.writeString(filePath, "{}", StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize file: " + filePath.toAbsolutePath(), e);
        }
    }

    private static Path tempSibling(Path target) {
        String name = target.getFileName().toString();
        return target.resolveSibling(name + ".tmp");
    }
}