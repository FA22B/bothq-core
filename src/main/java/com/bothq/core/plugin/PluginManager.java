package com.bothq.core.plugin;

import com.bothq.lib.annotations.DiscordEventListener;
import com.bothq.lib.interfaces.IPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The plugin manager that handles the initialization of plugin instances via reflection and dependency injection.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PluginManager {
    /**
     * The folder name in which the plugin .jar files are stored.
     */
    public static final String pluginFolderName = "plugins";

    /**
     * The JDA instance.
     */
    private final JDA jda;

    /**
     * The collection of loaded plugins in alphabetical order.
     */
    @Getter
    private final List<IPlugin> loadedPlugins = new ArrayList<>();

    /**
     * The collection of event listeners of the plugins.
     */
    private final Map<Method, Class<?>[]> eventListeners = new HashMap<>();

    /**
     * Unloads all plugins and loads all plugins from the plugin folder.
     */
    @PostConstruct
    public void reloadPlugins() {

        log.info("Reloading plugins...");

        // Clear event listeners
        eventListeners.clear();

        // Unload all current plugins
        unloadPlugins();

        // Load all plugins
        loadPlugins();

        // Initialize plugins
        for (var plugin : loadedPlugins) {

            // Initialize
            plugin.initialize(jda);

            // Trigger load method
            plugin.pluginLoad();
        }

        // JDA is ready at this point, so we trigger the ready event as well
        executeEventListeners(new ReadyEvent(jda));

        log.info("Reloading plugins done!");
    }

    private void loadPlugins() {
        // Create plugins directory wrapper
        File pluginsDir = new File(pluginFolderName);

        // Check if directory exists
        if (!pluginsDir.exists()) {

            // Create the folder
            var created = pluginsDir.mkdirs();

            // Check if the folder was not created
            if (!created) {
                log.error("Failed to create 'plugins' directory, aborting plugin load!");

                // Abort code execution at this point, as we don't have a working directory
                return;
            }
        }

        // Validate folder
        if (!pluginsDir.isDirectory()) {

            log.error("Found 'plugins' file instead of folder! Make sure to delete/rename the file named 'plugins'!");

            // Abort code execution at this point, as we don't have a working directory
            return;
        }

        // Get a collection of files that end with .jar inside the plugins folder
        var jarFiles = pluginsDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles != null) {

            log.debug("Found a total of {} .jar files inside of the plugins folder.", jarFiles.length);

            // Iterate over all files found
            for (var jarFile : jarFiles) {

                // Create a JarFile wrapper and URLClassLoader instance
                try (var jar = new JarFile(jarFile);
                     var classLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()})) {

                    // Iterate over jar entries and load classes implementing IPlugin
                    jar.stream().forEach(jarEntry -> loadPluginClass(jarEntry, classLoader, jarFile.getName()));
                } catch (Exception e) {
                    log.error("Error during plugin jar file parsing!", e);
                }
            }
        }
    }

    private void loadPluginClass(JarEntry jarEntry, URLClassLoader classLoader, String jarFileName) {

        // Verify entry is a class
        if (!jarEntry.getName().endsWith(".class")) {
            return;
        }

        try {
            // Convert class path to package-friendly name, which we can use for further reflection
            String className = jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6);

            // Load the class
            Class<?> cls = Class.forName(className, true, classLoader);

            // Check if class is an interface
            if (cls.isInterface()) {
                return;
            }

            // Verify that the class is of type IPlugin
            if (IPlugin.class.isAssignableFrom(cls)) {

                // Create a new instance of the valid class
                var pluginInstance = (IPlugin) cls.getDeclaredConstructor().newInstance();

                log.info("Loaded plugin: {} ({})", pluginInstance.getName(), jarFileName);

                for (var method : cls.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(DiscordEventListener.class)) {
                        // Non-static methods
                        eventListeners.put(method, method.getAnnotation(DiscordEventListener.class).value());
                    }
                }

                // Store class instance in collection
                loadedPlugins.add(pluginInstance);
            }
            else {
                // Check for static methods
                for (var method : cls.getDeclaredMethods()) {
                    if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(DiscordEventListener.class)) {
                        // Static methods
                        eventListeners.put(method, method.getAnnotation(DiscordEventListener.class).value());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error during plugin class loading for plugin '{}'!", jarFileName, e);
        }
    }

    private void unloadPlugins() {

        // Iterate over all loaded plugins
        for (var plugin : loadedPlugins) {

            try {
                // Execute plugin unload
                plugin.pluginUnload();
            } catch (Exception e) {
                log.error("Error during unload of plugin '{}'! Unloading anyway...", plugin.getName(), e);
            }

            log.debug("Unloaded plugin: {}", plugin.getName());
        }

        // Clear collection
        loadedPlugins.clear();

        log.info("Unloaded all plugins.");
    }

    @EventListener(GenericEvent.class)
    public void executeEventListeners(@NotNull GenericEvent event) {

        // Iterate over event listeners map
        for (var entry : eventListeners.entrySet()) {
            // Iterate over DiscordEventListener annotation content classes, e.g. ReadyEvent
            for (var eventType : entry.getValue()) {
                // Verify that content class matches current fired event type
                if (eventType.isAssignableFrom(event.getClass())) {
                    try {
                        // Static method check
                        if (Modifier.isStatic(entry.getKey().getModifiers())) {
                            // Static method invocation
                            entry.getKey().invoke(null, event);
                        } else {
                            // Non-static method invocation, needs an instance
                            for (var plugin : loadedPlugins) {
                                // Check if instance of loaded plugin matches
                                if (entry.getKey().getDeclaringClass().isInstance(plugin)) {
                                    // Non-static method invocation
                                    entry.getKey().invoke(plugin, event);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error during plugin event invocation for event '{}'!", event.getClass().getTypeName(), e);
                    }
                }
            }
        }
    }
}
