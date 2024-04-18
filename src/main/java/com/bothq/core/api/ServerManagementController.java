package com.bothq.core.api;

import com.bothq.core.api.model.Greeting;
import com.bothq.core.api.model.MemberInfo;
import com.bothq.core.api.model.PluginConfig;
import com.bothq.core.api.model.ServerInfo;
import com.bothq.core.api.repository.MemberInfoRepository;
import com.bothq.core.api.repository.PluginConfigRepository;
import com.bothq.core.api.repository.ServerInfoRepository;
import com.bothq.core.bothqcore.dao.DiscordGuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ControllerAdvice
@RestController
@RequestMapping("/api/v1/servers")
public class ServerManagementController {

    private final ServerInfoRepository serverInfoRepository;
    private final PluginConfigRepository pluginConfigRepository;
    private final MemberInfoRepository memberInfoRepository;
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public ServerManagementController(ServerInfoRepository serverInfoRepository, PluginConfigRepository pluginConfigRepository, MemberInfoRepository memberInfoRepository) {
        this.serverInfoRepository = serverInfoRepository;
        this.pluginConfigRepository = pluginConfigRepository;
        this.memberInfoRepository = memberInfoRepository;
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping(produces="application/json")
    public List<DiscordGuild> getAllServers() {
        return List.of(
                new DiscordGuild("1", "Server1", "icon1", true, null, null),
                new DiscordGuild("2", "Server2", "icon2", false, null, null),
                new DiscordGuild("3", "Server3", "icon3", true, null, null)
        );
    }

    @GetMapping("/{serverId}/plugins")
    public List<PluginConfig> getAllPlugins(@PathVariable String serverId) {
        return List.of(
                new PluginConfig("1", "Plugin1"),
                new PluginConfig("2", "Plugin2"),
                new PluginConfig("3", "Plugin3")
        );
    }

    @PutMapping("/{serverId}/plugins/{pluginId}")
    public boolean updatePluginConfig(@PathVariable String serverId, @RequestBody PluginConfig config) {
        return false;
    }

    @PostMapping("/{serverId}/plugins/{pluginId}")
    public boolean statusPlugin(@PathVariable String serverId, @PathVariable String pluginId, @RequestBody boolean enabled) {
        return false;
    }

    @DeleteMapping("/{serverId}/plugins/{pluginId}")
    public boolean deletePluginSettings(@PathVariable String serverId, @PathVariable String pluginId) {
        return false;
    }

    @DeleteMapping("/{serverId}")
    public boolean leaveServer(@PathVariable String serverId) {
        return false;
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