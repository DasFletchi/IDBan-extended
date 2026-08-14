package net.deamjava.id_ban.config

import com.google.gson.GsonBuilder
import net.deamjava.id_ban.IdBan
import net.fabricmc.loader.api.FabricLoader
import java.io.File


data class IdBanConfiguration(

    val bannedModIds: MutableList<String> = mutableListOf(
        "liquidbounce",
        "aoba",
        // patlozer/DoomsDay ships mcmod.info with Forge modid "sexmod".
        "sexmod"
        // TheDarkSword/DarkClient is an injected Rust/JNI client, not a loader mod;
        // no Fabric/Forge mod ID is claimed in its repository.
    ),

    val bannedKeywords: MutableList<String> = mutableListOf(
        "liquidbounce",
        "aoba",
        "wurst",
        "meteor",
        "doomsday",
        "sexmod",
        "darkclient"
    ),

    val modWhitelist: MutableList<String> = mutableListOf(),

    val playerWhitelist: MutableList<String> = mutableListOf(),

    val translationProbes: MutableMap<String, String> = mutableMapOf(
        "sodium"       to "sodium.option_impact.low",
        "lithium"      to "lithium.option.mixin.gen.chunk_tickets.tooltip",
        "iris"         to "options.iris.shaderPackSelection",
        "wurst-client" to "key.wurst.zoom",
        "meteor-client" to "key.meteor-client.open-gui",
        "xaeros-minimap" to "xaeros_minimap.gui.title",
        // Verified in CCBlueX/LiquidBounce (nextgen), src/main/resources/resources/liquidbounce/lang/en_us.json
        "liquidbounce" to "liquidbounce.command.autotranslate.description"
        // Aoba has mod ID "aoba", but its current upstream has no translation assets/key to probe.
        // TheDarkSword/DarkClient has no translation assets because it is injected, not loaded as a mod.
        // patlozer/DoomsDay exposes mcmod.info only; no lang assets/key are present to probe.
    ),

    val clientCommandPrefixes: MutableMap<String, MutableList<String>> = mutableMapOf(
        "wurst-client"  to mutableListOf("."),
        "meteor-client" to mutableListOf("."),
        "liquidbounce"  to mutableListOf(".", ","),
        "aoba"          to mutableListOf(".", ";"),
        "lunar-client"  to mutableListOf("/lc"),
        "badlion-client" to mutableListOf("/blc")
    ),

    // Only enforces an undetectable client when modWhitelist is explicitly enabled.
    // This avoids kicking normal clients just because a probe did not resolve.
    val kickOnUndetectable: Boolean = true,

    val kickMessage: String = "§cYou are running a banned modification: §e{reason}",



    val probeDelayTicks: Int = 200
)

object IdBanConfig {

    private val GSON = GsonBuilder().setPrettyPrinting().create()

    private val configFile: File by lazy {
        FabricLoader.getInstance().configDir.resolve("id-ban.json").toFile()
    }

    var config: IdBanConfiguration = IdBanConfiguration()
        private set

    fun load() {
        if (!configFile.exists()) {
            save()
            IdBan.LOGGER.info("Created default config at ${configFile.absolutePath}")
            return
        }
        try {
            config = GSON.fromJson(configFile.readText(), IdBanConfiguration::class.java)
                ?: IdBanConfiguration()
            IdBan.LOGGER.info("Loaded config from ${configFile.absolutePath}")
        } catch (e: Exception) {
            IdBan.LOGGER.error("Failed to load config, using defaults", e)
            config = IdBanConfiguration()
        }
    }

    fun save() {
        try {
            configFile.parentFile?.mkdirs()
            configFile.writeText(GSON.toJson(config))
        } catch (e: Exception) {
            IdBan.LOGGER.error("Failed to save config", e)
        }
    }

    fun reload() {
        load()
    }
}