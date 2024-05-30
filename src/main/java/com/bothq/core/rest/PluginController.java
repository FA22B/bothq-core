package com.bothq.core.rest;

import com.bothq.core.dto.PluginConfigDTO;
import com.bothq.core.repository.PluginRepository;
import com.bothq.core.service.PluginConfigDTOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plugins")
@Tag(name = "Plugins", description = "Operations related to getting plugin information")
public class PluginController {
    final PluginRepository pluginRepository;
    final PluginConfigDTOService pluginConfigService;


    @Operation(summary = "Get Plugin", description = "Fetches a plugins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Plugin not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{pluginId}")
    public ResponseEntity<PluginConfigDTO> getPlugin(
            @Parameter(description = "ID of the plugin") @PathVariable Long pluginId) {
        return ResponseEntity.ok(pluginConfigService.getPlugin(pluginId));
    }


    @Operation(summary = "Get Plugin", description = "Fetches a plugins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Plugin not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("")
    public ResponseEntity<List<PluginConfigDTO>> getAllPlugins() {
        return ResponseEntity.ok(pluginConfigService.getAllPlugins());
    }

}
