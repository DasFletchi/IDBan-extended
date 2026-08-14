> [!WARNING]
> This repository was partially modified by an AI (Buffy/Codebuff and Codex). Use at your own risk.
> This project is for an open server I manage. I understand the mod architecture and functionality, but I am not learning Java syntax for this project; the implementation (on my part) is fully done by AI. Java is unlike GDScript not my Focus.

**Id Ban**

IdBan is a server-side moderation mod for Minecraft that detects and blocks specific client-side mods by identifying their translation keys.

It allows server owners to enforce mod restrictions without requiring client installation or intrusive scanning — detection is performed using built-in game UI translation behavior.

**Features:-**

- Detects client-side mods using translation key probing

- Automatically kicks players running disallowed mods

- Fully configurable detection and ban system

- Supports mod ID bans, keyword detection, and translation probes

- Works with any mod that has custom translations

- Server-side only (no client install required)

**Important Limitations**

- IdBan currently does not inspect or trust client-brand strings.
- Client brands, mod channels, translation responses, and forced-mod proofs can be spoofed by a modified client.
- Requiring a normal mod such as Sodium is not cryptographic proof that channels or probes cannot be spoofed.
- With `modWhitelist` empty, vanilla players are allowed; this is intentional to avoid false positives.
- IdBan is lightweight identity/mod detection, not a full 24/7 gameplay anticheat.


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
    "sexmod"
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
    "liquidbounce": "liquidbounce.command.autotranslate.description"
  },
  "kickOnUndetectable": true,
  "kickMessage": "§cYou are running a banned modification: §e{reason}"
}
```

**Configuration Explanation**

translationProbes
Used for detection. Each entry maps:

mod id → translation key to probe

The LiquidBounce probe above is verified against `CCBlueX/LiquidBounce` branch `nextgen`, its `fabric.mod.json`, and `src/main/resources/resources/liquidbounce/lang/en_us.json`. The Aoba mod ID is verified from `Cocolots/Aoba-Client` `master` and included in the default banned ID list, but that branch contains no translation assets or translation key, so no Aoba translation probe is claimed here. `patlozer/DoomsDay` is open source and its repository `mcmod.info` declares the Forge mod ID `sexmod`, so that ID is included in the default banned ID list; no translation probe is claimed because no language assets were found in that repository. `TheDarkSword/DarkClient` is open source, but its README describes a Rust/JNI injection client rather than a loader mod, so no mod ID or translation probe is claimed for it.

`kickOnUndetectable` defaults to `true`, but only kicks after all configured probes resolve negatively when `modWhitelist` is explicitly non-empty. This prevents ordinary clients from being kicked by a negative probe in the default configuration.

If the client resolves the translation, the mod


**License**

This project is licensed under the MIT License.
See the [LICENSE](LICENSE) file for details.
