package com.bothq.core.api;
import com.bothq.core.api.repository.MemberInfoRepository;
import com.bothq.core.api.repository.PluginConfigRepository;
import com.bothq.core.api.repository.ServerInfoRepository;
import com.bothq.core.bothqcore.dao.DiscordGuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@ControllerAdvice
@RestController
@RequestMapping("/api/v1/servers")
public class ServerManagementController {

    private final ServerInfoRepository serverInfoRepository;
    private final PluginConfigRepository pluginConfigRepository;
    private final MemberInfoRepository memberInfoRepository;

    @Autowired
    public ServerManagementController(ServerInfoRepository serverInfoRepository, PluginConfigRepository pluginConfigRepository, MemberInfoRepository memberInfoRepository) {
        this.serverInfoRepository = serverInfoRepository;
        this.pluginConfigRepository = pluginConfigRepository;
        this.memberInfoRepository = memberInfoRepository;
    }

    @GetMapping(produces="application/json")
    public List<DiscordGuild> getAllServers() {
        return List.of(
                new DiscordGuild("1", "Server1", "icon1", true, null, null),
                new DiscordGuild("2", "Server2", "icon2", false, null, null),
                new DiscordGuild("3", "Server3", "icon3", true, null, null)
        );
    }
}