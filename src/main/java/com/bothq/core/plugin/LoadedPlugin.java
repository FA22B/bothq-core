package com.bothq.core.plugin;

import com.bothq.core.plugin.config.Config;
import com.bothq.lib.plugin.IPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Getter
public class LoadedPlugin implements Closeable {
    /**
     * The jar-file name of the plugin.
     */
    private final String fileName;

    /**
     * The class loader which this plugin is loaded into.
     */
    private final URLClassLoader classLoader;

    /**
     * The config.
     */
    private Config config;

    /**
     * The instance of {@link IPlugin} created.
     */
    private IPlugin pluginInstance;

    /**
     * The plugin ID generated from the plugin instance class type.
     */
    private String pluginId;

    /**
     * The collection of registered event listeners of the plugin.
     */
    private final Map<Method, Class<?>[]> eventListeners = new HashMap<>();

    public void setPluginInstance(IPlugin pluginInstance) {

        // Check if plugin instance was already set
        if (this.pluginInstance != null) {
            throw new RuntimeException("Plugin instance was already set!");
        }

        // Apply the plugin instance
        this.pluginInstance = pluginInstance;

        // Generate plugin ID from plugin class
        pluginId = pluginInstance.getClass().getName();



        // Create the config
        config = new Config(
                pluginId,
                pluginInstance.getName(),
                pluginInstance.getDescription(),
                pluginId
        );
    }

    /**
     * Triggers the {@link IPlugin}.pluginLoad() of the plugin instance.
     */
    public void load() {
        try {
            // Trigger plugin load
            pluginInstance.pluginLoad();
        } catch (Exception e) {
            log.error("Error during load for plugin '{}' ({})!", pluginInstance.getName(), fileName, e);
        }
    }

    /**
     * Triggers the {@link IPlugin}.pluginUnload() of the plugin instance.
     */
    public void unload() {
        try {
            // Trigger plugin unload
            pluginInstance.pluginUnload();
        } catch (Exception e) {
            log.error("Error during unload of plugin '{}' ({})!", pluginInstance.getName(), fileName, e);
        }
    }

    /**
     * Triggers the {@link IPlugin}.initialize(jda) of the plugin instance.
     *
     * @param jda The JDA instance.
     */
    public void initialize(JDA jda) {
        try {
            // Trigger initialize
            pluginInstance.initialize(jda);

            // Call the config creation method with the above created config instance
            pluginInstance.createConfig(config);
        } catch (Exception e) {
            log.error("Error during initialization of plugin '{}' ({})!", pluginInstance.getName(), fileName, e);
        }
    }

    /**
     * Checks if the plugin has any data registered in the class loader.
     *
     * @return Whether the plugin has data in the class loader.
     */
    public boolean hasData() {
        return pluginInstance != null || !eventListeners.isEmpty();
    }

    @Override
    public void close() throws IOException {
        // Close the class loader
        classLoader.close();
    }
}
