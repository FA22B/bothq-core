package com.bothq.core.rest;

import com.bothq.core.auth.UserInfoProvider;
import com.bothq.core.dao.DiscordGuild;
import com.bothq.core.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/servers")
public class ServerManagementController {

    private final UserInfoRepository userInfoRepository;
    private final ObjectProvider<UserInfoProvider> userInfoProviders;


    public UserInfoProvider getUserInfoProvider() {
        return userInfoProviders.getObject();
    }


    @GetMapping(produces = "application/json")
    public Collection<DiscordGuild> getAllServers() {
        return getUserInfoProvider()
                .getGuilds()
                .values();
    }
}