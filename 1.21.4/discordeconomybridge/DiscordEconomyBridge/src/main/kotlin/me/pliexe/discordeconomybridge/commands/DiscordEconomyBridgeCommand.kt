package me.pliexe.discordeconomybridge.commands

import me.pliexe.discordeconomybridge.DiscordEconomyBridge
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class DiscordEconomyBridgeCommand(private val plugin: DiscordEconomyBridge) : CommandExecutor {
    
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("discordeconomybridge.admin")) {
            sender.sendMessage("${ChatColor.RED}Je hebt geen toestemming om dit commando te gebruiken!")
            return true
        }

        if (args.isEmpty()) {
            sendHelp(sender)
            return true
        }

        when (args[0].lowercase()) {
            "reload" -> {
                plugin.reloadConfig()
                sender.sendMessage("${ChatColor.GREEN}DiscordEconomyBridge configuratie is herladen!")
                return true
            }
            "info" -> {
                sender.sendMessage("${ChatColor.GOLD}===== DiscordEconomyBridge =====")
                sender.sendMessage("${ChatColor.YELLOW}Versie: ${ChatColor.WHITE}${plugin.description.version}")
                sender.sendMessage("${ChatColor.YELLOW}Auteur: ${ChatColor.WHITE}${plugin.description.authors.joinToString()}")
                return true
            }
            else -> {
                sendHelp(sender)
                return true
            }
        }
    }
    
    private fun sendHelp(sender: CommandSender) {
        sender.sendMessage("${ChatColor.GOLD}===== DiscordEconomyBridge Commando's =====")
        sender.sendMessage("${ChatColor.YELLOW}/deb reload ${ChatColor.WHITE}- Herlaad de configuratie")
        sender.sendMessage("${ChatColor.YELLOW}/deb info ${ChatColor.WHITE}- Toon plugin informatie")
    }
} 