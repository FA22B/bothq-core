# bothq-core
Discord integration framework for BotHQ, managing plugins and providing a REST-API.

## jda.yaml
To compile, the project currently requires the presence of a `jda.yaml` file inside `src/main/resources/`. The contents of the file should look like this:
```yaml
spring:
  discord:
    token: "YOUR_TOKEN_HERE"
```
Optionally you can specify a `proxyHost` and `proxyPort` if needed, where the host is a `String`.
