# bothq-core
Discord integration framework for BotHQ, managing plugins and providing a REST-API.

## secrets.yaml
To compile, the project currently requires the presence of a `secrets.yaml` file inside `src/main/resources/`. The contents of the file should look like this:
```yaml
spring:
  discord:
    bot-token: "bot-token-from-discord-developers"
    client-secret: "oauth2-client-secret-from-discord-developers"
```
Optionally you can specify a `proxyHost` and `proxyPort` if needed, where the host is a `String`.
