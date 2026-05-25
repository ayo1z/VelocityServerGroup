package me.a.mc.velocityservergroup.api.group

import com.velocitypowered.api.proxy.server.RegisteredServer

interface ServerGroup {

    val name: String

    val servers: Set<RegisteredServer>

    /**
     * 子服是否属于本组 按名比较 大小写敏感
     *
     * @param serverName 子服名
     * @return 属于返回 true
     */
    fun contains(serverName: String): Boolean = servers.any { it.serverInfo.name == serverName }
}
