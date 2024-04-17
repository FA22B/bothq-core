package com.bothq.core.plugin;

import com.bothq.lib.plugin.IPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * The instances of {@link IPlugin} created.
     */
    private final List<IPlugin> pluginInstances = new ArrayList<>();

    /**
     * The collection of registered event listeners of the plugin.
     */
    private final Map<Method, Class<?>[]> eventListeners = new HashMap<>();

    /**
     * Triggers the {@link IPlugin}.pluginLoad() of all plugin instances.
     */
    public void load() {
        for (var plugin : pluginInstances) {
            try {
                // Trigger plugin load
                plugin.pluginLoad();
            } catch (Exception e) {
                log.error("Error during load for plugin '{}' ({})!", plugin.getName(), fileName, e);
            }
        }
    }

    /**
     * Triggers the {@link IPlugin}.pluginUnload() of all plugin instances.
     */
    public void unload() {
        for (var plugin : pluginInstances) {
            try {
                // Trigger plugin unload
                plugin.pluginUnload();
            } catch (Exception e) {
                log.error("Error during unload for plugin '{}' ({})!", plugin.getName(), fileName, e);
            }
        }
    }

    /**
     * Triggers the {@link IPlugin}.initialize(jda) of all plugin instances.
     *
     * @param jda The JDA instance.
     */
    public void initialize(JDA jda) {
        for (var plugin : pluginInstances) {
            try {
                // Trigger initialize
                plugin.initialize(jda, null); // TODO: Pass config instance
            } catch (Exception e) {
                log.error("Error during initialization of plugin '{}' ({})!", plugin.getName(), fileName, e);
            }
        }
    }

    /**
     * Checks if the plugin has any data registered in the class loader.
     *
     * @return Whether the plugin has data in the class loader.
     */
    public boolean hasData() {
        return !pluginInstances.isEmpty() || !eventListeners.isEmpty();
    }

    @Override
    public void close() throws IOException {
        // Close the class loader
        classLoader.close();
    }
}
