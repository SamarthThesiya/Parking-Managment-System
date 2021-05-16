### Setup Debug configuration

`Run` -> `Edit confgurations` -> Add new `PHP Remote Debug` with name **Demo Debug**

1. Check `Filter Debug Connections by IDE Key`
1. Enter **PHPSTORM** as `IDE key (session id)`
1. Add new Server with the following parameters:
   `Name`: **DemoApp2**, `Host`: **0.0.0.0**,
   `Port`: **80**, `Debugger`: **XDebug**,
   Check `Use path mappings ...` and map root repo folder to `/app` on server.

`Settings` (For current project) -> `Language & Frameworks` -> `PHP` -> `Debug`

For XDebug section:
1. Enter **9001** as `Debug port`
1. Check `Can accept incomming connections`
1. Uncheck all `Force break...` checkboxes

Navigate down to `DBGp Proxy` and:
1. Enter **PHPSTORM** as `IDE key`
1. Enter **localhost** as `Host`
1. Enter **9001** as `Port`

### Setup Interpreter configuration

`Settings` (For current project) -> `Language & Frameworks` -> `PHP`

1. Add new `Interpreter` from `Docker-compose` with name `PHP 7.1 Docker (Backend)`
1. Enter **backend-test** as `Service`
1. Add your local docker installation as `Server` with connection through unixsocket
1. Check `Visible only for this project`
1. Add environment variable **ENVIRONMENT=test**
