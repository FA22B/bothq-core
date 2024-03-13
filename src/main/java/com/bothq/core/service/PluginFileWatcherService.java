package com.bothq.core.service;

import com.bothq.core.event.PluginFileChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PluginFileWatcherService {
    private final Path pluginsDir = Paths.get(PluginLoaderService.pluginFolderName);

    private final ApplicationEventPublisher eventPublisher;

    private Thread watchingThread = null;

    private static boolean isRunningFromIDE() {
        // Get the location of a class file
        String classpath = Objects.requireNonNull(PluginFileWatcherService.class.getResource(PluginFileWatcherService.class.getSimpleName() + ".class")).toString();

        // Check if it begins with "file:", indicating it's loaded from the filesystem, typical for IDEs
        // In a .jar deployment, it would start with "jar:"
        return classpath.startsWith("file:");
    }

    @PostConstruct
    private void init() {
        // Start watcher automatically when run from IDE
        if (isRunningFromIDE()) {
            log.info("Started automatic plugin folder watching due to IDE run mode.");
            startWatching();
        }
    }

    public boolean isWatching() {
        return watchingThread != null && watchingThread.isAlive();
    }

    public void stopWatching() {
        if (watchingThread != null) {
            watchingThread.interrupt();
        }
    }

    @Async
    public void startWatching() {
        CompletableFuture.runAsync(() -> {

            // Check if it's already enabled
            if (isWatching()) {
                return;
            }

            log.info("Plugin folder watch service started.");

            // Apply current thread
            watchingThread = Thread.currentThread();

            // Create watch service
            try (var watchService = FileSystems.getDefault().newWatchService()) {

                // Register watcher to plugins folder
                pluginsDir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

                while (!Thread.interrupted()) {

                    WatchKey key;
                    try {
                        // Block wait for file change to occur
                        key = watchService.poll();
                        if (key == null) {
                            Thread.sleep(100);
                            continue;
                        }
                    } catch (InterruptedException e) {
                        log.info("Watch service interrupted.");
                        break;
                    }

                    // Go through all changes
                    for (var event : key.pollEvents()) {
                        handleEvent(event);
                    }

                    // Reset and validate key
                    if (!key.reset()) {
                        break;
                    }
                }
            } catch (IOException e) {
                log.error("Error while watching plugins folder!", e);
            } finally {
                watchingThread = null; // Clear the thread reference
            }
        });
    }

    private void handleEvent(WatchEvent<?> event) {

        // Get the event kind
        var kind = event.kind();

        @SuppressWarnings("unchecked")
        var ev = (WatchEvent<Path>) event;
        var fileName = ev.context();

        // Filter for modify or create
        if (kind == ENTRY_MODIFY || kind == ENTRY_CREATE) {
            // Get the full file path
            var filePath = pluginsDir.resolve(fileName);

            // Wait for the file
            if (isFileReady(filePath)) {
                // Publish event
                eventPublisher.publishEvent(new PluginFileChangedEvent(this, fileName.getFileName().toString(), filePath));
            }
        } else if (kind == ENTRY_DELETE) {
            // Publish event
            //eventPublisher.publishEvent(new PluginFileChangedEvent(this, fileName.getFileName().toString(), null));
        }
    }

    private boolean isFileReady(Path file) {

        // 30 seconds timeout
        final long timeout = 30000;

        long startTime = System.currentTimeMillis();
        try {
            long fileSize = -1;
            while (true) {
                // Get the current time
                long currentTime = System.currentTimeMillis();

                // Check time difference
                if (currentTime - startTime > timeout) {
                    log.info("Timeout reached while waiting for file to be ready: {}", file);

                    // Exit after timeout
                    break;
                }

                // Get the current file size
                long currentFileSize = Files.size(file);

                // File size unchanged
                if (fileSize == currentFileSize) {
                    try {
                        // Additional 2-second wait to ensure no more writes occur
                        Thread.sleep(2000);

                        // File size is still unchanged
                        if (Files.size(file) == fileSize) {
                            // File size hasn't changed for 2 seconds, assume it's ready
                            return true;
                        }
                    } catch (InterruptedException e) {
                        // Restore interrupted status
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    // Apply current file size
                    fileSize = currentFileSize;

                    // Wait for a bit before checking again
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error while checking for file modification for '{}'!", file, e);
        }

        return false;
    }

    @PreDestroy
    private void cleanup() {
        // Ensure the watching thread is stopped when the bean is destroyed
        stopWatching();
    }
}
