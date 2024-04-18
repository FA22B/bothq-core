package com.bothq.core.rest;

import com.bothq.core.bothqcore.dao.DiscordGuild;
import com.bothq.core.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/servers")
public class ServerManagementController {

    private final UserInfoRepository userInfoRepository;

    @GetMapping(produces = "application/json")
    public List<DiscordGuild> getAllServers() {
        return List.of(
                new DiscordGuild("1", "Server1", "icon1", true, null, null),
                new DiscordGuild("2", "Server2", "icon2", false, null, null),
                new DiscordGuild("3", "Server3", "icon3", true, null, null)
        );
    }
}