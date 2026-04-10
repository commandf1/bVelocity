# bVelocity

bVelocity is a customized Velocity fork focused on three things:

- more aggressive bandwidth compression defaults
- a built-in `/bvelocity` command surface
- localized operational output for the added features

This fork is licensed under the GPLv3 license.

## Highlights

- `compression-threshold` defaults to `128`
- `compression-level = -1` now means:
  - native `libdeflate`: level `12`
  - Java fallback: level `9`
- built-in `/bvelocity` and `/bv` commands
- outbound compression statistics and synthetic benchmark tooling
- localized `bvelocity.command.*` message keys across all bundled locale files
  
## Building

bVelocity is built with [Gradle](https://gradle.org). Use the wrapper:

```bash
./gradlew build
```

If you only want the proxy jar:

```bash
./gradlew :velocity-proxy:shadowJar
```

## Running

The packaged proxy artifact is written to:

```text
proxy/build/libs/bVelocity-1.0.0-SNAPSHOT.jar
```

Run it with:

```bash
java -jar proxy/build/libs/bVelocity-1.0.0-SNAPSHOT.jar
```

The proxy generates `velocity.toml` on first start.

## Commands

The fork adds a built-in command namespace:

- `/bvelocity`
- `/bv`

Current subcommands include:

- `status`
- `backend`
- `config`
- `compression`
- `compression stats`
- `compression reset`
- `compression benchmark [bytes] [rounds]`

## Localisation

The `bvelocity.command.*` keys are included in all bundled locale files.
English and Simplified Chinese have customized wording; the remaining locales
currently ship with English fallback text for the new command group.
