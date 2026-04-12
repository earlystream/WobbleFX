<p align="center">
  <a href="https://modrinth.com/user/earlystream">
    <img
      src="https://cdn.modrinth.com/data/cached_images/2df5ae65196aa7a4a0aef20e208c0005ff06471f.png"
      alt="Modrinth profile"
      style="image-rendering: pixelated;"
    />
  </a>

  <a href="https://github.com/earlystream">
    <img
      src="https://cdn.modrinth.com/data/cached_images/14bb5f6380dbf0e9a0bc20179ef4d9728b0f88d9.png"
      alt="GitHub profile"
      style="image-rendering: pixelated;"
    />
  </a>
</p>

# Meme Wobble Mod

Client-side Fabric mod for Minecraft 1.21.11 that adds a short "Invincible wobble" style hit reaction to living entities.

## What It Does

When a living entity is hit and enters `hurtTime`, the rendered model gets a brief spring wobble:

- strong initial bend on impact
- overshoot past neutral
- 1-2 smaller settling oscillations
- quick decay back to normal

This is visual only.

It does not:

- change knockback
- move hitboxes
- affect damage logic
- send packets
- change server behavior

## Supported Behavior

- All `LivingEntity` renders get a root/body wobble.
- Biped-style models get extra body, head, arm, and leg wobble.
- Repeated hits retrigger the effect cleanly.

## Build

Use Java 21.

```bash
JAVA_HOME=/usr/lib/jvm/java-21 ./gradlew build
```

Built jar:

```text
build/libs/memewobble-1.0.0.jar
```

## Test

1. Launch the client with the mod installed.
2. Spawn a zombie.
3. Hit it repeatedly.
4. Verify the wobble retriggers on each hit and settles back to normal quickly.

## Project Layout

```text
src/main/java/com/kila/memewobble/
src/main/resources/
build.gradle
settings.gradle
gradle.properties
gradlew
gradlew.bat
gradle/
```
