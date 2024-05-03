package com.bothq.core.rest;

import com.bothq.core.entity.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RestController
@PreAuthorize("@discordPermissionEvaluator.hasPermission(#serverId, 'Administrator')")
@RequestMapping("/api/v1/servers")
public class UserServerController {

    @GetMapping("/{serverId}/plugins")
    public List<String> getAllPlugins(@PathVariable String serverId) {
        return List.of("Plugin1", "Plugin2", "Plugin3");
    }

    @PutMapping("/{serverId}/plugins/{pluginId}")
    public String updatePluginConfig(@PathVariable String serverId, @RequestBody String config) {
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
    public String getServerInfo(@PathVariable String serverId) {
        return "Server1";
    }

    @GetMapping("/{serverId}/members")
    public List<UserInfo> getMemberList(@PathVariable String serverId) {
        return List.of(
                new UserInfo(1, "Member1", "max@payne.de"),
                new UserInfo(2, "Member2", "muh@kuh.de"),
                new UserInfo(3, "Member3", "Emil@email.de")
        );
    }
}