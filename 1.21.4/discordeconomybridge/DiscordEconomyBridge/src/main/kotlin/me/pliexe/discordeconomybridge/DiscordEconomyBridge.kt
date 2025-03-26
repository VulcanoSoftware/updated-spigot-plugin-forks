package me.pliexe.discordeconomybridge

import me.pliexe.discordeconomybridge.commands.DiscordEconomyBridgeCommand
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit
import java.util.logging.Level

class DiscordEconomyBridge : JavaPlugin() {
    
    companion object {
        private lateinit var instance: DiscordEconomyBridge
        
        fun getInstance(): DiscordEconomyBridge {
            return instance
        }
    }
    
    override fun onEnable() {
        instance = this
        
        // Load configuration
        saveDefaultConfig()
        
        // Register commands
        getCommand("discordeconomybridge")?.setExecutor(DiscordEconomyBridgeCommand(this))
        
        logger.info("DiscordEconomyBridge versie ${description.version} is geactiveerd!")
        
        // Controleer afhankelijkheden
        if (server.pluginManager.getPlugin("Vault") == null) {
            logger.warning("Vault niet gevonden! Sommige functies werken mogelijk niet correct.")
        }
        
        if (server.pluginManager.getPlugin("DiscordSRV") == null) {
            logger.warning("DiscordSRV niet gevonden! Sommige functies werken mogelijk niet correct.")
        }
    }
    
    override fun onDisable() {
        logger.info("DiscordEconomyBridge is uitgeschakeld!")
    }
} 