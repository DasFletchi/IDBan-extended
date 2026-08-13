> [!WARNING]
> Dieses Repository wurde teilweise durch eine KI (Buffy/Codebuff) geändert. Die Erkennung ist nicht garantiert; teste sie auf deinem Server, bevor du sie produktiv verwendest.
> Der Build läuft über GitHub Actions mit Java 25 — Java/Gradle müssen dafür nicht lokal installiert werden.

**IdBan**

IdBan is a server-side moderation mod for Minecraft that detects and blocks specific client-side mods by identifying their translation keys.

It allows server owners to enforce mod restrictions without requiring client installation or intrusive scanning — detection is performed using built-in game UI translation behavior.

**Features:-**

- Detects client-side mods using translation key probing

- Automatically kicks players running disallowed mods

- Fully configurable detection and ban system

- Supports mod ID bans, keyword detection, and translation probes

- Works with any mod that has custom translations

- Server-side only (no client install required)


**How It Works**

Minecraft allows UI text (chat, signs, bossbars, item names, etc.) to be defined using:
Translation keys
Keybind placeholders
The client replaces these placeholders with localized or configured values before sending data back to the server.
Detection Method
The server sends the player an interface element (such as a sign or anvil rename field) containing a translation key.

Example:
``sodium.option_impact.low``

If the player has the corresponding mod installed:

``sodium.option_impact.low`` → Low

If the mod is not installed:

``sodium.option_impact.low`` → ``sodium.option_impact.low``

By checking whether the placeholder was replaced, the server can determine whether the mod exists on the client.

This works because:

- Mods register their own translation keys
- The client automatically resolves them
- The server receives the resolved value

**Supported UI Probes**

Detection can be triggered through:
- Sign text updates (auto-closed instantly)
- Anvil rename screen translation resolution
- Any UI element that causes client text resolution

This detection method works for any mod with custom translations, which includes most mods (buttons, settings, tooltips, etc.).


**Configuration**

Example configuration:
```json
{
  "bannedModIds": [
    "liquidbounce",
    "aoba",
    "freecam"
  ],
  "bannedKeywords": [],
  "modWhitelist": [],
  "playerWhitelist": [
    "DEAMJAVA"
  ],
  "translationProbes": {
    "sodium": "sodium.option_impact.low",
    "lithium": "lithium.option.mixin.gen.chunk_tickets.tooltip",
    "iris": "options.iris.shaderPackSelection",
    "wurst-client": "key.wurst.zoom",
    "meteor-client": "key.meteor-client.open-gui",
    "xaeros-minimap": "xaeros_minimap.gui.title",
    "liquidbounce": "liquidbounce.command.autotranslate.description",
    "freecam": "msg.freecam.enable"
  },
  "kickOnUndetectable": true,
  "kickMessage": "§cYou are running a banned modification: §e{reason}"
}
```

**Configuration Explanation**

translationProbes
Used for detection. Each entry maps:

mod id → translation key to probe

The LiquidBounce probe above is verified against `CCBlueX/LiquidBounce` branch `nextgen`, its `fabric.mod.json`, and `src/main/resources/resources/liquidbounce/lang/en_us.json`. The Aoba mod ID is verified from `Cocolots/Aoba-Client` `master` and included in the default banned ID list, but that branch contains no translation assets or translation key, so no Aoba translation probe is claimed here.

`kickOnUndetectable` defaults to `true`, but only kicks after all configured probes resolve negatively when `modWhitelist` is explicitly non-empty. This prevents ordinary clients from being kicked by a negative probe in the default configuration.

If the client resolves the translation, the mod


**License**

This project is licensed under the MIT License.
See the [LICENSE](LICENSE) file for details.