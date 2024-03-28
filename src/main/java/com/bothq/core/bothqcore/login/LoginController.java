package com.bothq.core.bothqcore.login;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    // @GetMapping("/login")
    // @ResponseBody
    // public ResponseEntity<?> login(@RequestBody String credentials, HttpServletResponse response){
    //     Cookie cookie = new Cookie()
    // }


    @GetMapping("")
    public String login(){
        return "home";
    }

    @GetMapping("/public")
    public String _public(){
        return "Public";
    }


    @GetMapping("/api/v1/test")
    public String api_test(Principal principal) {
        return String.format("{\"user\": \"%s\"}", principal.getName());
    }
}
