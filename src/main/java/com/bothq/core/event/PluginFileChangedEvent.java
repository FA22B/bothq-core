package com.bothq.core.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.nio.file.Path;

@Getter
public class PluginFileChangedEvent extends ApplicationEvent {

    private final String fileName;
    private final Path filePath;

    /**
     * Create a new PluginFileChangedEvent.
     *
     * @param source   The object on which the event initially occurred or with
     *                 which the event is associated (never {@code null}).
     * @param filePath The path of the plugin file that changed.
     */
    public PluginFileChangedEvent(Object source, String fileName, Path filePath) {
        super(source);

        this.fileName = fileName;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "PluginFileChangedEvent{" +
                "fileName=" + fileName +
                "filePath=" + filePath +
                '}';
    }
}
