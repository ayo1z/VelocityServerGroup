package me.a.mc.velocityservergroup.command

import me.a.mc.velocityservergroup.VelocityServerGroup.config
import me.a.mc.velocityservergroup.api.VelocityServerGroupAPI
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand

@CommandHeader(name = "velocityservergroup", permission = "velocityservergroup.admin")
object CommandHandler {

    @CommandBody
    private val list = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val groups = VelocityServerGroupAPI.getGroups().ifEmpty {
                return@execute
            }
            groups.forEach { group ->
                sender.sendMessage("组: ${group.name} 在线: ${group.servers.sumOf { it.playersConnected.size }}")
            }
        }
    }

    @Awake(LifeCycle.ENABLE)
    private fun reload() = config.onReload {
        VelocityServerGroupAPI.load()
    }
}
