package com.bothq.core.api;

import com.bothq.core.api.model.MemberInfo;
import com.bothq.core.api.model.PluginConfig;
import com.bothq.core.api.model.ServerInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RestController
@PreAuthorize("@discordPermissionEvaluator.hasPermission(#serverId, 'Administrator')")
@RequestMapping("/api/v1/servers")
public class UserServerController {

    @GetMapping("/{serverId}/plugins")
    public List<PluginConfig> getAllPlugins(@PathVariable String serverId) {
        return List.of(
                new PluginConfig("1", "Plugin1"),
                new PluginConfig("2", "Plugin2"),
                new PluginConfig("3", "Plugin3")
        );
    }

    @PutMapping("/{serverId}/plugins/{pluginId}")
    public String updatePluginConfig(@PathVariable String serverId, @RequestBody PluginConfig config) {
        return "Plugin configuration updated successfully";
    }

    @PostMapping("/{serverId}/plugins/{pluginId}")
    public String statusPlugin(@PathVariable String serverId, @PathVariable String pluginId, @RequestBody boolean enabled) {
        return "Plugin status updated successfully";
    }

    @DeleteMapping("/{serverId}/plugins/{pluginId}")
    public String deletePluginSettings(@PathVariable String serverId, @PathVariable String pluginId) {
        return "Plugin settings deleted successfully";
    }

    @DeleteMapping("/{serverId}")
    public String leaveServer(@PathVariable String serverId) {
        return "Server left successfully";
    }

    @GetMapping("/{serverId}")
    public ServerInfo getServerInfo(@PathVariable String serverId) {
        return new ServerInfo("1", "Server1");
    }

    @GetMapping("/{serverId}/members")
    public List<MemberInfo> getMemberList(@PathVariable String serverId) {
        return List.of(
                new MemberInfo("1", "Member1", "max@payne.de"),
                new MemberInfo("2", "Member2", "muh@kuh.de"),
                new MemberInfo("3", "Member3", "Emil@email.de")
        );
    }
}