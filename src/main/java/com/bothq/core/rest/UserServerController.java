package com.bothq.core.rest;

import com.bothq.core.auth.UserInfoProvider;
import com.bothq.core.dao.DiscordGuild;
import com.bothq.core.dto.get.ConcretePluginConfigGetDTO;
import com.bothq.core.dto.put.PluginConfigPutDTO;
import com.bothq.core.service.PluginConfigDTOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
@PreAuthorize("@discordPermissionEvaluator.hasPermission(#serverId, T(net.dv8tion.jda.api.Permission).ADMINISTRATOR)")
@RequestMapping("/api/v1/servers")
@Tag(name = "User Server Operations", description = "Operations related to user authenticated actions")
@Slf4j
public class UserServerController {
    private final ObjectProvider<UserInfoProvider> userInfoProviders;

    private final PluginConfigDTOService pluginConfigService;


    public UserInfoProvider getUserInfoProvider() {
        return userInfoProviders.getObject();
    }

    @GetMapping("/{serverId}/plugins")
    public List<String> getAllPlugins(@PathVariable String serverId) {

        return List.of("Plugin1", "Plugin2", "Plugin3");
    }

    @Operation(summary = "Get Plugin Configuration", description = "Fetches the configuration of a specified plugin on a specified server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Plugin or Server not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{serverId}/plugins/{pluginId}")
    public ResponseEntity<ConcretePluginConfigGetDTO> getPluginConfiguration(
            @Parameter(description = "ID of the server") @PathVariable Long serverId,
            @Parameter(description = "ID of the plugin") @PathVariable Long pluginId) {
        return ResponseEntity.ok(pluginConfigService.getConcretePluginConfiguration(serverId, pluginId));
    }


    @PutMapping("/{serverId}/plugins/{pluginId}")
    public ResponseEntity<ConcretePluginConfigGetDTO> updatePluginConfig(@PathVariable Long serverId,
                                                                         @PathVariable Long pluginId,
                                                                         @RequestBody PluginConfigPutDTO configPutDTO) {
        pluginConfigService.updateConfig(serverId, pluginId, configPutDTO);
        return getPluginConfiguration(serverId, pluginId);
    }
    @DeleteMapping("/{serverId}/plugins/{pluginId}")
    public ResponseEntity<?> deletePluginSettings(@PathVariable long serverId, @PathVariable long pluginId) {
        pluginConfigService.deleteServerPlugin(serverId, pluginId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{serverId}")
    public ResponseEntity<?> leaveServer(@PathVariable String serverId) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{serverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DiscordGuild getServerInfo(@PathVariable String serverId) {
        return getUserInfoProvider()
                .getGuilds()
                .get(serverId);
    }

}