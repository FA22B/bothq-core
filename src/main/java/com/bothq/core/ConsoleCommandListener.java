package com.bothq.core;

import com.bothq.core.service.PluginFileWatcherService;
import com.bothq.core.service.PluginLoaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsoleCommandListener implements CommandLineRunner {

    private final PluginLoaderService pluginLoaderService;
    private final PluginFileWatcherService pluginFileWatcherService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Application started. Type 'help' to get a list of available commands.");

        // Create input stream reader
        try (var reader = new BufferedReader(new InputStreamReader(System.in))) {

            // Prepare string object storing the console input (C# could use this within the while loop...)
            String line;

            // Wait for a new line input in console
            while ((line = reader.readLine()) != null) {

                // Validate input, skip on blank line input
                if (line.isBlank()) {
                    continue;
                }

                // Split the input line into 2 parts, separated by a whitespace char
                var parts = line.trim().split("\\s+", 2);

                // Isolate the command, which is always the first part
                var command = parts[0].toLowerCase();

                // Store arguments into a different collection, if any
                var arguments = parts.length > 1 ? Arrays.asList(parts[1].split("\\s+")) : List.of();

                switch (command) {

                    case "reload":
                        // Reload all plugins
                        // TODO: Add option to reload a single plugin only
                        pluginLoaderService.reloadPlugins();
                        break;

                    case "toggle-plugin-watcher":
                        // Toggle plugin folder watch service
                        if (pluginFileWatcherService.isWatching()) {
                            pluginFileWatcherService.stopWatching();
                        } else {
                            pluginFileWatcherService.startWatching();
                        }
                        break;

                    case "help":
                        // Display available commands or help information
                        log.info("Available commands:");
                        log.info("reload - Reloads all plugins");
                        log.info("toggle-plugin-watcher - Toggles plugin folder watch service");
                        log.info("help - Displays this help message");
                        break;

                    default:
                        log.warn("Unknown command. Type 'help' for a list of available commands.");
                        break;
                }
            }
        }
    }
}
