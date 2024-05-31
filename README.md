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


## QUICKSTART

### Benötigte Repositories pullen:  
https://github.com/FA22B/bothq-core/tree/v0.1.3  
https://github.com/FA22B/bothq-frontend/tree/v0.1.0  


### Plugins hinzufügen
Da es etwas tricky ist die Plugins zu kompilieren, sind die kompilierten Plugin .jar Dateien in den Releases hinterlegt.
Diese können einfach heruntergeladen werden und dann in `/plugins` hinterlegt werden (nicht `/src/plugins`)

https://github.com/FA22B/bothq-discord-plugins/releases/tag/v0.1.0


### Ausführung
Das Frontend muss aktuell auf `localhost:4200` laufen.  
Das Backend muss aktuell auf `localhost:8080` laufen.  
