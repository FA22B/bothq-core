package com.bothq.core.service;

import com.bothq.core.plugin.LoadedPlugin;
import com.bothq.lib.annotations.DiscordEventListener;
import com.bothq.lib.interfaces.IPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The plugin manager that handles the initialization of plugin instances via reflection and dependency injection.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PluginLoaderService {
    /**
     * The folder name in which the plugin .jar files are stored.
     */
    public static final String pluginFolderName = "plugins";

    /**
     * The JDA instance.
     */
    private final JDA jda;

    /**
     * The Spring event publisher.
     */
    private final ApplicationEventPublisher eventPublisher;

    /**
     * The collection of loaded plugins in alphabetical order.
     */
    @Getter
    private final List<LoadedPlugin> loadedPlugins = new ArrayList<>();

    /**
     * Unloads all plugins and loads all plugins from the plugin folder.
     */
    @PostConstruct
    public void reloadPlugins() {

        log.info("Reloading plugins...");

        // Unload all current plugins
        unloadPlugins();

        // Load all plugins
        loadPlugins();

        // Order all plugins
        orderPlugins();

        // Initialize plugins
        for (var plugin : loadedPlugins) {

            // Initialize
            plugin.initialize(jda);

            // Trigger load method
            plugin.load();
        }

        // JDA is ready at this point, so we trigger the ready event as well
        eventPublisher.publishEvent(new PayloadApplicationEvent<>(jda, new ReadyEvent(jda)));

        log.info("Reloading plugins done!");
    }

    private void loadPlugins() {

        // Create plugins directory wrapper
        File pluginsDir = new File(pluginFolderName);

        // Check if directory exists
        if (!pluginsDir.exists()) {

            // Check if the folder was not created
            if (!pluginsDir.mkdirs()) {
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
        var jarFiles = pluginsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
        if (jarFiles != null) {

            log.info("Found a total of {} .jar files inside of the plugins folder.", jarFiles.length);

            // Iterate over all files found
            for (var jarFile : jarFiles) {

                var fileName = jarFile.getName();
                URLClassLoader classLoader = null;
                LoadedPlugin loadedPlugin = null;

                try (var jar = new JarFile(jarFile)) {
                    // Create class loader
                    classLoader = createClassLoaderWithDependencies(jarFile);

                    // Create plugin object
                    loadedPlugin = new LoadedPlugin(fileName, classLoader);

                    // Iterate over jar entries and load classes implementing IPlugin
                    LoadedPlugin finalLoadedPlugin = loadedPlugin;
                    jar.stream().forEach(jarEntry -> {
                        // Attempt to load plugin from jar entry
                        loadPluginClass(jarEntry, finalLoadedPlugin, fileName);
                    });

                    // Check if the plugin had data to be loaded
                    if (loadedPlugin.hasData()) {
                        // Add loaded plugin to collection
                        loadedPlugins.add(loadedPlugin);
                    } else {
                        // Close the plugin, as nothing was loaded into it which is being used
                        loadedPlugin.close();
                    }
                } catch (Exception e) {
                    log.error("Error during plugin jar file parsing!", e);

                    if (loadedPlugin != null) {
                        try {
                            loadedPlugin.close();
                        } catch (Exception ignored) {
                        }
                    } else if (classLoader != null) {
                        try {
                            classLoader.close();
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
    }

    /**
     * Loads a plugin class by its {@link JarEntry}, using the specified class loader inside the plugin.
     *
     * @param jarEntry    The jar entry.
     * @param plugin      The plugin.
     * @param jarFileName The name of the jar file.
     */
    private void loadPluginClass(JarEntry jarEntry, LoadedPlugin plugin, String jarFileName) {

        // Verify entry is a class
        if (!jarEntry.getName().endsWith(".class")) {
            return;
        }

        try {
            // Convert class path to package-friendly name, which we can use for further reflection
            String className = jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6);

            // Load the class
            Class<?> cls = Class.forName(className, true, plugin.getClassLoader());

            // Check if class is an interface
            if (cls.isInterface()) {
                return;
            }

            // Verify that the class is of type IPlugin
            if (IPlugin.class.isAssignableFrom(cls)) {

                // Create a new instance of the valid class
                var pluginInstance = (IPlugin) cls.getDeclaredConstructor().newInstance();

                log.info("Loaded plugin: {} ({})", className, jarFileName);

                for (var method : cls.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(DiscordEventListener.class)) {

                        // Non-static methods
                        plugin.getEventListeners().put(method, method.getAnnotation(DiscordEventListener.class).value());

                        log.info("Added event listener: {}.{} ({})", className, method.getName(), jarFileName);
                    }
                }

                // Store class instance in collection
                plugin.getPluginInstances().add(pluginInstance);
            } else {
                // Check for static methods
                for (var method : cls.getDeclaredMethods()) {
                    if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(DiscordEventListener.class)) {

                        // Static methods
                        plugin.getEventListeners().put(method, method.getAnnotation(DiscordEventListener.class).value());

                        log.info("Added static event listener: {}.{} ({})", className, method.getName(), jarFileName);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error during plugin class loading for plugin '{}'!", jarFileName, e);
        }

    }

    private void unloadPlugins() {

        for (var plugin : loadedPlugins) {

            // Unload the plugin
            plugin.unload();

            try {
                // Close the plugin
                plugin.close();
            } catch (Exception ignored) {
                // Ignored
            }

            log.info("Unloaded plugin file '{}'", plugin.getFileName());
        }

        // Clear the collection
        loadedPlugins.clear();

        log.info("Unloaded all plugins.");
    }

    private void orderPlugins() {
        // Order plugins in alphabetical order by their file name
        loadedPlugins.sort(Comparator.comparing(LoadedPlugin::getFileName));
    }

    private URLClassLoader createClassLoaderWithDependencies(File jarFile) throws Exception {
        // Convert the jarFile to a URL
        var jarUrl = jarFile.toURI().toURL();

        // Create a list for all URLs you want to load classes from
        List<URL> urls = new ArrayList<>();
        urls.add(jarUrl);

        // Additional dependencies can be added to 'urls' if necessary

        // Create a new URLClassLoader with the specified URLs and the current Thread's context class loader as the parent
        return new URLClassLoader(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());
    }
}
