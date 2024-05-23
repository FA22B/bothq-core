package com.bothq.core.rest;

import com.bothq.core.auth.UserInfoProvider;
import com.bothq.core.dao.DiscordGuild;
import com.bothq.core.dto.PluginConfigDTO;
import com.bothq.core.entity.UserInfo;
import com.bothq.core.service.UserServerControllerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
@PreAuthorize("@discordPermissionEvaluator.hasPermission(#serverId, 'Administrator')")
@RequestMapping("/api/v1/servers")
public class UserServerController {
    private final ObjectProvider<UserInfoProvider> userInfoProviders;

    private final UserServerControllerService userServerControllerService;


    public UserInfoProvider getUserInfoProvider() {
        return userInfoProviders.getObject();
    }

    @GetMapping("/{serverId}/plugins")
    public List<String> getAllPlugins(@PathVariable String serverId) {
        return List.of("Plugin1", "Plugin2", "Plugin3");
    }

    @GetMapping("/{serverId}/plugins/{pluginId}")
    public ResponseEntity<PluginConfigDTO> getPluginConfiguration(@PathVariable Long serverId, @PathVariable Long pluginId) {
        return ResponseEntity.ok(userServerControllerService.getPluginConfiguration(serverId, pluginId));
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

    @GetMapping(value = "/{serverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DiscordGuild getServerInfo(@PathVariable String serverId) {
        return getUserInfoProvider()
                .getGuilds()
                .get(serverId);
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