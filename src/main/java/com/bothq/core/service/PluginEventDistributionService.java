package com.bothq.core.service;

import com.bothq.core.plugin.LoadedPlugin;
import com.bothq.lib.plugin.IPlugin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Service
@RequiredArgsConstructor
@Slf4j
public class PluginEventDistributionService {

    private final PluginLoaderService pluginLoaderService;

    @EventListener(GenericEvent.class)
    private void executePluginEventListeners(@NotNull GenericEvent event) {

        // Iterate over all loaded plugins
        for (var plugin : pluginLoaderService.getLoadedPlugins()) {
            // Iterate over event listeners map
            for (var entry : plugin.getEventListeners().entrySet()) {
                // Create variables
                var method = entry.getKey();
                var classes = entry.getValue();

                // Iterate over DiscordEventListener annotation content classes, e.g. ReadyEvent
                for (var eventType : classes) {
                    // Verify that content class matches current fired event type
                    if (eventType.isAssignableFrom(event.getClass())) {
                        try {
                            // Invoke method, passing the event object
                            method.invoke(getPluginInstance(plugin, method), event);
                        } catch (Exception e) {
                            log.error("Error during plugin event invocation for event '{}'!", event.getClass().getTypeName(), e);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    private IPlugin getPluginInstance(LoadedPlugin loadedPlugin, Method method) {
        // Prepare return object
        IPlugin instance = null;

        // Non-static method check
        if (!Modifier.isStatic(method.getModifiers())) {
            // Check if instance of loaded plugin matches
            if (method.getDeclaringClass().isInstance(loadedPlugin.getPluginInstance())) {
                // Apply instance
                instance = loadedPlugin.getPluginInstance();
            }

            if (instance == null) {
                // No instance found, throw exception
                throw new RuntimeException("Plugin instance for registered event method was not found!");
            }
        }

        // Return found instance, or null if it's a static method
        return instance;
    }
}
